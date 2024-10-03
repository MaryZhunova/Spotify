package com.example.spotify.data

import com.example.spotify.data.converter.AudioFeaturesResponseToInfoConverter
import com.example.spotify.data.converter.SearchResponseToInfoConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapper
import com.example.spotify.data.storage.SpotifyInfoStorage
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.SearchInfo
import com.example.spotify.domain.models.TrackInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Реализация репозитория для получения информации о треках и исполнителях из Spotify
 *
 * @constructor
 * @param apiMapper маппер для преобразования данных из API в модели
 * @param trackInfoConverter конвертер для преобразования ответа API о треке в модель [TrackInfo]
 */
class SpotifyInfoRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyInfoApiMapper,
    private val trackInfoConverter: TrackResponseToInfoConverter,
    private val audioFeaturesConverter: AudioFeaturesResponseToInfoConverter,
    private val searchResponseToInfoConverter: SearchResponseToInfoConverter,
    private val infoStorage: SpotifyInfoStorage,
) : SpotifyInfoRepository {

    override suspend fun getArtistsTopTracks(accessCode: String, id: String): List<TrackInfo> {
        val tracks = apiMapper.getArtistsTopTracks(accessCode, id).tracks
        val audioFeaturesInfo = getTracksAudioFeatures(accessCode, tracks.map { it.id })
        return tracks.map { track ->
            trackInfoConverter
                .convert(track)
                .copy(audioFeatures = audioFeaturesInfo.firstOrNull { it.id == track.id })
        }
    }

    override suspend fun getTracksAudioFeatures(
        accessCode: String,
        ids: List<String>
    ): List<AudioFeaturesInfo> =
        apiMapper.getTracksAudioFeatures(accessCode, ids.joinToString(",")).audioFeatures
            .map(audioFeaturesConverter::convert)

    override suspend fun search(
        accessCode: String,
        type: String,
        query: String
    ): SearchInfo = withContext(Dispatchers.IO) {
        searchResponseToInfoConverter.convert(apiMapper.search(accessCode, type, query))
    }

    override suspend fun searchGenres(accessCode: String): List<String> =
        infoStorage.getAvailableGenres() ?: apiMapper.searchGenres(accessCode).genres.also {
            infoStorage.setAvailableGenres(genres = it)
        }

    override suspend fun createPlaylist(
        accessCode: String,
        artists: List<String>,
        genres: List<String>,
        tracks: List<String>
    ): List<TrackInfo> =
        apiMapper.createPlaylist(
            accessToken = accessCode,
            artists = artists.joinToString(","),
            genres = genres.joinToString(","),
            tracks = tracks.joinToString(",")
        ).tracks.map(trackInfoConverter::convert)
}