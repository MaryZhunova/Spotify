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
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Реализация репозитория для получения статистики пользователя и информации о треках и исполнителях из Spotify
 *
 * @constructor
 * @param apiMapper маппер для преобразования данных из API в модели
 * @param userProfileConverter конвертер для преобразования ответа API о пользователе в модель [UserProfileInfo]
 * @param authRepository репозиторий для работы с токенами доступа
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
) : SpotifyUserStatsRepository {

    override suspend fun getCurrentUserProfile(): UserProfileInfo =
        withContext(Dispatchers.IO) {
            val token = authRepository.getAccessToken()
            val userProfileResponse = apiMapper.getCurrentUserProfile(token)
            userProfileConverter.convert(userProfileResponse)
        }

    override suspend fun getTopTracks(timeRange: String, limit: Int): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            val token = authRepository.getAccessToken()
            val initResponse = apiMapper.getTopTracks(token, timeRange, limit)
            val nextPageItems = initResponse.next?.let { url ->
                apiMapper.getTopTracksNextPage(token, url)
            }?.items.orEmpty()
            val result = initResponse.items + nextPageItems
            result
                .map(trackResponseConverter::convert)
                .putTracksToDB()
                .map(trackEntityConverter::convert)
        }

    override suspend fun getTopTracks(id: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            trackDao.findTracksByArtistId(id).map(trackEntityConverter::convert)
        }

    override suspend fun getTopArtists(timeRange: String, limit: Int): List<ArtistInfo> =
        withContext(Dispatchers.IO) {
            val token = authRepository.getAccessToken()
            val initResponse = apiMapper.getTopArtists(token, timeRange, limit)
            val nextPageItems = initResponse.next?.let { url ->
                apiMapper.getTopArtistsNextPage(token, url)
            }?.items.orEmpty()
            val result = initResponse.items + nextPageItems
            result
                .map(artistResponseConverter::convert)
                .putArtistsToDB()
                .map(artistEntityConverter::convert)
        }

    private fun List<ArtistEntity>.putArtistsToDB(): List<ArtistEntity> {
        artistDao.insertAll(*this.toTypedArray())
        return this
    }

    private fun List<TrackEntity>.putTracksToDB(): List<TrackEntity> {
        trackDao.insertAll(*this.toTypedArray())
        return this
    }
}