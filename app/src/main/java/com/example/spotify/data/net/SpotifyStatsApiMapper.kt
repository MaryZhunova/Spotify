package com.example.spotify.data.net

import com.example.spotify.models.data.net.TopArtistsResponse
import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse

/**
 * Интерфейс для преобразования ответов API из Spotify Web API
 */
interface SpotifyStatsApiMapper {

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
     * @return Объект [TopTracksResponse], содержащий информацию о топ треках
     */
    suspend fun getTopTracks(accessToken: String, timeRange: String, limit: Int): TopTracksResponse

    /**
     * Получает следующую страницу топ треков из API Spotify
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param url URL для получения следующей страницы топ треков
     * @return Объект [TopTracksResponse], содержащий следующую страницу информации о топ треках
     */
    suspend fun getTopTracksNextPage(accessToken: String, url: String): TopTracksResponse

    /**
     * Получает топ исполнителей пользователя из API Spotify
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param timeRange временной интервал для топ исполнителей (например, "short_term", "medium_term", "long_term")
     * @param limit Количество топ исполнителей, которые нужно вернуть
     * @return Объект [TopArtistsResponse], содержащий информацию о топ исполнителях
     */
    suspend fun getTopArtists(accessToken: String, timeRange: String, limit: Int): TopArtistsResponse

    /**
     * Получает следующую страницу топ исполнителей из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param url URL для получения следующей страницы топ исполнителей
     * @return Объект [TopArtistsResponse], содержащий следующую страницу информации о топ исполнителях
     */
    suspend fun getTopArtistsNextPage(accessToken: String, url: String): TopArtistsResponse
}