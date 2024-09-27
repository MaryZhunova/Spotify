package com.example.spotify.domain

import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.TrackInfo

/**
 * Интерфейс репозитория для получения информации о треках и исполнителях из Spotify
 */
interface SpotifyInfoRepository {

    /**
     * Получает информацию о топе треков исполнителя
     *
     * @param accessCode токен доступа
     * @param id идентификатор исполнителя
     *
     * @return информация о топе треков исполнителя в виде списка объектов [TrackInfo]
     */
    suspend fun getArtistsTopTracks(accessCode: String, id: String): List<TrackInfo>

    /**
     * Получает список с информацией о треках
     *
     * @param accessCode токен доступа
     * @param ids список идентификаторов треков
     *
     * @return информация о треках в виде списка объектов [AudioFeaturesInfo]
     */
    suspend fun getTracksAudioFeatures(accessCode: String, ids: List<String>): List<AudioFeaturesInfo>

}