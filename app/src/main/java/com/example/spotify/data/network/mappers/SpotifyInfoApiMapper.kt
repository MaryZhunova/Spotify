package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.ArtistsTopTracksResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse

/**
 * Интерфейс для преобразования ответов API из Spotify Web API
 */
interface SpotifyInfoApiMapper {

    /**
     * Получает топ треки исполнителя из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param id идентификатор исполнителя
     * @return Объект [ArtistsTopTracksResponse], содержащий топ треки исполнителя
     */
    suspend fun getArtistsTopTracks(accessToken: String, id: String): ArtistsTopTracksResponse

    /**
     * Получает аудиохарактеристики треков из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param ids идентификаторы треков, разделенные запятыми, для получения аудиохарактеристик
     * @return объект [AudioFeaturesListResponse], содержащий аудиохарактеристики треков
     */
    suspend fun getTracksAudioFeatures(accessToken: String, ids: String): AudioFeaturesListResponse
}