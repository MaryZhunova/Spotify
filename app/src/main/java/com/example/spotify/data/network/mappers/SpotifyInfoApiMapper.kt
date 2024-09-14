package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.ArtistsTopTracksResponse

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
}