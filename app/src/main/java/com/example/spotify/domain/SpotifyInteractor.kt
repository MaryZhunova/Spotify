package com.example.spotify.domain

import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo

/**
 * Интерфейс интерактора для получения информации о треках и исполнителях из Spotify
 */
interface SpotifyInteractor {

    /**
     * Получает информацию о топе треков исполнителя
     *
     * @param id идентификатор исполнителя
     *
     * @return информация о топе треков исполнителя в виде списка объектов [TrackInfo]
     */
    suspend fun getArtistsTopTracks(id: String): List<TrackInfo>

    /**
     * Получает информацию об исполнителе из бд
     *
     * @param id идентификатор исполнителя
     *
     * @return информация о топе треков исполнителя в виде объекта [ArtistInfo]
     */
    suspend fun getArtistsInfo(id: String): ArtistInfo

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
    suspend fun getTopArtists(timeRange: String): List<ArtistInfo>

    /**
     * Получает список популярных треков пользователя
     *
     * @param timeRange период времени для получения треков
     * @return информация о популярных треках в виде объекта  List<TrackInfo>
     */
    suspend fun getTopGenres(timeRange: String): List<GenreInfo>

    /**
     * Очищает кэш и бд
     */
    suspend fun clear()

    suspend fun searchTracks(query: String): List<TrackInfo>

    suspend fun searchArtists(query: String): List<ArtistInfo>

    suspend fun searchGenres(): List<String>

    suspend fun createPlaylist(
        genres: List<String>,
        artists: List<ArtistInfo>,
        tracks: List<TrackInfo>
    ): List<TrackInfo>
}