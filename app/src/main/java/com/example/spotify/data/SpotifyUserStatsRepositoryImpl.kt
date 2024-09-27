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
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo
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
    private val artistDao: ArtistDao,
    private val trackDao: TrackDao,
    private val userStatsStorage: SpotifyUserStatsStorage,
    private val userInfoStorage: SpotifyUserInfoStorage,
) : SpotifyUserStatsRepository {

    override suspend fun getCurrentUserProfile(accessCode: String): UserProfileInfo =
        userInfoStorage.getCurrentUserInfo() ?: run {
            val userProfileResponse = apiMapper.getCurrentUserProfile(accessCode)
            userProfileConverter.convert(userProfileResponse).also { userInfo ->
                userInfoStorage.setCurrentUserInfo(userInfo)
            }
        }

    override suspend fun getTopTracks(accessCode: String, timeRange: String): List<TrackInfo> {
        val entities = userStatsStorage.getIdsList(getTracksCacheKey(timeRange))?.let { ids ->
            val tracks = trackDao.getByIds(*ids.toTypedArray())
            tracks.sortedBy { ids.indexOf(it.id) }
        } ?: getTopTracksInternal(accessCode, timeRange)
        return entities.map(trackEntityConverter::convert)
    }

    override fun getTopTracksByArtistId(id: String): List<TrackInfo> =
        trackDao.findTracksByArtistId(id).map(trackEntityConverter::convert)

    override suspend fun getTopArtists(accessCode: String, timeRange: String): List<ArtistInfo> =
        getTopArtistsEntities(accessCode, timeRange).map(artistEntityConverter::convert)

    override fun clear() {
        trackDao.deleteAll()
        artistDao.deleteAll()
        userStatsStorage.clear()
        userInfoStorage.clear()
    }

    override suspend fun getTopGenres(accessCode: String, timeRange: String): List<GenreInfo> {
        val artistEntities = getTopArtistsEntities(accessCode, timeRange)
        val genreMap = mutableMapOf<String, MutableList<String>>()
        artistEntities.forEach { artist ->
            artist.genres.forEach { genre ->
                genreMap.getOrPut(genre) { mutableListOf() }.add(artist.name)
            }
        }
        val frequencyMap = genreMap.map { (genre, artists) -> genre to artists.size }.toMap()
        return frequencyMap.mapNotNull { (genre, count) ->
            if (count > 2) {
                GenreInfo(
                    genre = genre,
                    numberOfArtists = count,
                    artistNames = genreMap[genre] ?: emptyList()
                )
            } else {
                null
            }
        }.sortedByDescending { it.numberOfArtists }
    }

    override fun getArtistsInfo(id: String): ArtistInfo =
        artistEntityConverter.convert(artistDao.getById(id))

    private suspend fun getTopTracksInternal(
        accessCode: String,
        timeRange: String
    ): List<TrackEntity> {
        val initResponse = apiMapper.getTopTracks(accessCode, timeRange, LIMIT)
        val nextPageItems = initResponse.next?.let { url ->
            apiMapper.getTopTracksNextPage(accessCode, url)
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

    private suspend fun getTopArtistsEntities(
        accessCode: String,
        timeRange: String
    ): List<ArtistEntity> =
        userStatsStorage.getIdsList(getArtistsCacheKey(timeRange))?.let { ids ->
            val artists = artistDao.getByIds(*ids.toTypedArray())
            artists.sortedBy { ids.indexOf(it.id) }
        } ?: getTopArtistsInternal(accessCode, timeRange)


    private suspend fun getTopArtistsInternal(
        accessCode: String,
        timeRange: String
    ): List<ArtistEntity> {
        val initResponse = apiMapper.getTopArtists(accessCode, timeRange, LIMIT)
        val nextPageItems = initResponse.next?.let { url ->
            apiMapper.getTopArtistsNextPage(accessCode, url)
        }?.items.orEmpty()
        val result = initResponse.items + nextPageItems
        return result
            .map(artistResponseConverter::convert)
            .also { artists ->
                artistDao.insertAll(*artists.toTypedArray())
                userStatsStorage.setIdsList(
                    getArtistsCacheKey(timeRange),
                    artists.map { it.id })
            }
    }

    private fun getArtistsCacheKey(timeRange: String) = "${timeRange}_artists"

    private companion object {
        const val LIMIT = 50
    }
}