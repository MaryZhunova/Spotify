package com.example.spotify.domain

import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.models.data.UserProfileInfo

/**
 * Интерфейс репозитория для получения информации
 * о пользователе и статистики по трекам и исполнителях из Spotify
 */
interface SpotifyUserStatsRepository {

    /**
     * Получает информацию о текущем пользователе
     *
     * @return информация о пользователе в виде объекта [UserProfileInfo]
     */
    suspend fun getCurrentUserProfile(): UserProfileInfo

    /**
     * Получает список популярных треков пользователя
     *
     * @param timeRange период времени для получения треков
     * @param limit количество треков, которые нужно вернуть
     * @return информация о популярных треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopTracks(timeRange: String, limit: Int = 50): List<TrackInfo>

    /**
     * Получает список любимых треков пользователя по идентификатору конкретного исполнителя
     *
     * @param id идентификатор исполнителя
     *
     * @return информация о любимых треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopTracks(id: String): List<TrackInfo>

    /**
     * Получает список топа исполнителей пользователя
     *
     * @param timeRange период времени для получения топа исполнителей
     * @param limit количество исполнителей, которые нужно вернуть
     * @return информация о топ исполнителях в виде списка [ArtistInfo]
     */
    suspend fun getTopArtists(timeRange: String, limit: Int = 50):  List<ArtistInfo>
}