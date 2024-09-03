package com.example.spotify.domain

import com.example.spotify.models.data.TopArtistsInfo
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo

/**
 * Интерфейс репозитория для получения информации
 * о пользователе и статистики по трекам и исполнителях из Spotify
 */
interface SpotifyStatsRepository {

    /**
     * Получает информацию о текущем пользователе
     *
     * @return информация о пользователе в виде объекта [UserProfileInfo], или null, если запрос не удался
     */
    suspend fun getCurrentUserProfile(): UserProfileInfo?

    /**
     * Получает список популярных треков пользователя
     *
     * @param timeRange период времени для получения треков
     * @param limit количество треков, которые нужно вернуть
     * @return информация о популярных треках в виде объекта [TopTracksInfo], или null, если запрос не удался
     */
    suspend fun getTopTracks(timeRange: String, limit: Int): TopTracksInfo?

    /**
     * Получает следующую страницу результатов популярных треков
     *
     * @param url URL для получения следующей страницы
     * @return информация о популярных треках в виде объекта [TopTracksInfo], или null, если запрос не удался
     */
    suspend fun getTopTracksNextPage(url: String): TopTracksInfo?

    /**
     * Получает список топа исполнителей пользователя
     *
     * @param timeRange период времени для получения топа исполнителей
     * @param limit количество исполнителей, которые нужно вернуть
     * @return информация о топ исполнителях в виде объекта [TopArtistsInfo], или null, если запрос не удался
     */
    suspend fun getTopArtists(timeRange: String, limit: Int): TopArtistsInfo?

    /**
     * Получает следующую страницу результатов топа исполнителей
     *
     * @param url URL для получения следующей страницы.
     * @return информация о топ исполнителях в виде объекта [TopArtistsInfo], или null, если запрос не удался
     */
    suspend fun getTopArtistsNextPage(url: String): TopArtistsInfo?
}