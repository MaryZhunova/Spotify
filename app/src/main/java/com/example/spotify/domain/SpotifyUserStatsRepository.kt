package com.example.spotify.domain

import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.GenreInfo
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
    suspend fun getCurrentUserProfile(accessCode: String): UserProfileInfo

    /**
     * Получает список популярных треков пользователя
     *
     * @param accessCode токен доступа
     * @param timeRange период времени для получения треков
     *
     * @return информация о популярных треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopTracks(accessCode: String, timeRange: String): List<TrackInfo>

    /**
     * Получает список любимых треков пользователя по идентификатору конкретного исполнителя
     *
     * @param id идентификатор исполнителя
     *
     * @return информация о любимых треках в виде объекта  List<TrackInfo>
     */
    fun getTopTracksByArtistId(id: String): List<TrackInfo>

    /**
     * Получает список топа исполнителей пользователя
     *
     * @param accessCode токен доступа
     * @param timeRange период времени для получения топа исполнителей
     *
     * @return информация о топ исполнителях в виде списка [ArtistInfo]
     */
    suspend fun getTopArtists(accessCode: String, timeRange: String):  List<ArtistInfo>

    /**
     * Очищает кэш и бд
     */
    fun clear()

    /**
     * Получает список популярных треков пользователя
     *
     * @param accessCode токен доступа
     * @param timeRange период времени для получения треков
     *
     * @return информация о популярных треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopGenres(accessCode: String, timeRange: String): List<GenreInfo>

    /**
     * Получает информацию об исполнителе из бд
     *
     * @param id идентификатор исполнителя
     *
     * @return информация о топе треков исполнителя в виде объекта [ArtistInfo]
     */
    fun getArtistsInfo(id: String): ArtistInfo
}