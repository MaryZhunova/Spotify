package com.example.spotify.data

import com.example.spotify.data.converter.ArtistEntityToInfoConverter
import com.example.spotify.data.converter.ArtistResponseToEntityConverter
import com.example.spotify.data.converter.TrackEntityToInfoConverter
import com.example.spotify.data.converter.TrackResponseToEntityConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.db.ArtistDao
import com.example.spotify.data.models.db.ArtistEntity
import com.example.spotify.data.db.TrackDao
import com.example.spotify.data.models.db.TrackEntity
import com.example.spotify.data.network.mappers.SpotifyUserStatsApiMapper
import com.example.spotify.data.storage.SpotifyUserInfoStorage
import com.example.spotify.data.storage.SpotifyUserStatsStorage
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Реализация репозитория для получения статистики о треках и исполнителях из Spotify
 *
 * @constructor
 * @param apiMapper маппер для преобразования данных из API в модели
 * @param userProfileConverter конвертер для преобразования ответа API о пользователе в модель [UserProfileInfo]
 * @param trackResponseConverter конвертер для преобразования ответа API в модель бд.
 * @param trackEntityConverter конвертер для преобразования модели бд в модель [TrackInfo]
 * @param artistResponseConverter конвертер для преобразования ответа API в модель бд.
 * @param artistEntityConverter Преобразователь для преобразования модели бд в модель [ArtistInfo]
 * @param authRepository репозиторий для работы с токенами доступа
 * @param artistDao DAO для работы с данными исполнителей
 * @param trackDao DAO для работы с данными треков
 * @param userStatsStorage Хранилище для статистики пользователя
 * @param userInfoStorage Хранилище для информации о пользователе
 */
class SpotifyUserStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyUserStatsApiMapper,
    private val userProfileConverter: UserProfileResponseToInfoConverter,
    private val trackResponseConverter: TrackResponseToEntityConverter,
    private val trackEntityConverter: TrackEntityToInfoConverter,
    private val artistResponseConverter: ArtistResponseToEntityConverter,
    private val artistEntityConverter: ArtistEntityToInfoConverter,
    private val authRepository: AuthRepository,
    private val artistDao: ArtistDao,
    private val trackDao: TrackDao,
    private val userStatsStorage: SpotifyUserStatsStorage,
    private val userInfoStorage: SpotifyUserInfoStorage
) : SpotifyUserStatsRepository {

    override suspend fun getCurrentUserProfile(): UserProfileInfo =
        withContext(Dispatchers.IO) {
            userInfoStorage.getCurrentUserInfo() ?: run {
                val token = authRepository.getAccessToken()
                val userProfileResponse = apiMapper.getCurrentUserProfile(token)
                userProfileConverter.convert(userProfileResponse).also { userInfo ->
                    userInfoStorage.setCurrentUserInfo(userInfo)
                }
            }
        }

    override suspend fun getTopTracks(timeRange: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            val entities = userStatsStorage.getIdsList(getTracksCacheKey(timeRange))?.let { ids ->
                trackDao.getByIds(*ids.toTypedArray())
            } ?: getTopTracksInternal(timeRange)
            return@withContext entities.map(trackEntityConverter::convert)
        }

    override suspend fun getTopTracksByArtistId(id: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            trackDao.findTracksByArtistId(id).map(trackEntityConverter::convert)
        }

    override suspend fun getTopArtists(timeRange: String): List<ArtistInfo> =
        withContext(Dispatchers.IO) {
            val entities = userStatsStorage.getIdsList(getArtistsCacheKey(timeRange))?.let { ids ->
                artistDao.getByIds(*ids.toTypedArray())
            } ?: getTopArtistsInternal(timeRange)
            return@withContext entities.map(artistEntityConverter::convert)
        }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        trackDao.deleteAll()
        artistDao.deleteAll()
        userStatsStorage.clear()
        userInfoStorage.clear()
    }

    private suspend fun getTopTracksInternal(timeRange: String): List<TrackEntity> {
        val token = authRepository.getAccessToken()
        val initResponse = apiMapper.getTopTracks(token, timeRange, LIMIT)
        val nextPageItems = initResponse.next?.let { url ->
            apiMapper.getTopTracksNextPage(token, url)
        }?.items.orEmpty()
        val result = initResponse.items + nextPageItems
        return result
            .map(trackResponseConverter::convert)
            .also { tracks ->
                trackDao.insertAll(*tracks.toTypedArray())
                userStatsStorage.setIdsList(getTracksCacheKey(timeRange), tracks.map { it.id })
            }
    }

    private fun getTracksCacheKey(timeRange: String) = "${timeRange}_tracks"

    private suspend fun getTopArtistsInternal(timeRange: String): List<ArtistEntity> =
        withContext(Dispatchers.IO) {
            val token = authRepository.getAccessToken()
            val initResponse = apiMapper.getTopArtists(token, timeRange, LIMIT)
            val nextPageItems = initResponse.next?.let { url ->
                apiMapper.getTopArtistsNextPage(token, url)
            }?.items.orEmpty()
            val result = initResponse.items + nextPageItems
            result
                .map(artistResponseConverter::convert)
                .also { artists ->
                    artistDao.insertAll(*artists.toTypedArray())
                    userStatsStorage.setIdsList(getArtistsCacheKey(timeRange), artists.map { it.id })
                }
        }

    private fun getArtistsCacheKey(timeRange: String) = "${timeRange}_artists"

    private companion object {
        const val LIMIT = 50
    }
}