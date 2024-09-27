package com.example.spotify.data

import com.example.spotify.data.converter.AudioFeaturesResponseToInfoConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapper
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.TrackInfo
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
    private val audioFeaturesConverter: AudioFeaturesResponseToInfoConverter
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
}