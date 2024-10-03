package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.ArtistResponse
import com.example.spotify.data.models.network.PaginatedResponse
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.data.models.network.UserProfileResponse

/**
 * Интерфейс для преобразования ответов API из Spotify Web API
 */
interface SpotifyUserStatsApiMapper {

    /**
     * Получает текущий профиль пользователя из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @return Объект [UserProfileResponse], содержащий информацию о профиле пользователя
     */
    suspend fun getCurrentUserProfile(accessToken: String): UserProfileResponse

    /**
     * Получает топ треки пользователя из API Spotify
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param timeRange временной интервал для топ треков (например, "short_term", "medium_term", "long_term")
     * @param limit количество топ треков, которые нужно вернуть
     * @return Объект [TrackResponse], содержащий информацию о топ треках
     */
    suspend fun getTopTracks(accessToken: String, timeRange: String, limit: Int): PaginatedResponse<TrackResponse>

    /**
     * Получает следующую страницу топ треков из API Spotify
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param url URL для получения следующей страницы топ треков
     * @return Объект [TrackResponse], содержащий следующую страницу информации о топ треках
     */
    suspend fun getTopTracksNextPage(accessToken: String, url: String): PaginatedResponse<TrackResponse>

    /**
     * Получает топ исполнителей пользователя из API Spotify
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param timeRange временной интервал для топ исполнителей (например, "short_term", "medium_term", "long_term")
     * @param limit Количество топ исполнителей, которые нужно вернуть
     * @return Объект [ArtistResponse], содержащий информацию о топ исполнителях
     */
    suspend fun getTopArtists(accessToken: String, timeRange: String, limit: Int): PaginatedResponse<ArtistResponse>

    /**
     * Получает следующую страницу топ исполнителей из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param url URL для получения следующей страницы топ исполнителей
     * @return Объект [ArtistResponse], содержащий следующую страницу информации о топ исполнителях
     */
    suspend fun getTopArtistsNextPage(accessToken: String, url: String): PaginatedResponse<ArtistResponse>
}