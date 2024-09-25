package com.example.spotify.domain

import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo

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
     * @return информация о популярных треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopTracks(timeRange: String): List<TrackInfo>

    /**
     * Получает список любимых треков пользователя по идентификатору конкретного исполнителя
     *
     * @param id идентификатор исполнителя
     *
     * @return информация о любимых треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopTracksByArtistId(id: String): List<TrackInfo>

    /**
     * Получает список топа исполнителей пользователя
     *
     * @param timeRange период времени для получения топа исполнителей
     * @return информация о топ исполнителях в виде списка [ArtistInfo]
     */
    suspend fun getTopArtists(timeRange: String):  List<ArtistInfo>

    /**
     * Очищает кэш и бд
     */
    suspend fun clear()
}