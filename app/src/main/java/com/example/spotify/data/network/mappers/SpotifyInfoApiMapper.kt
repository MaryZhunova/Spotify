package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.TrackListResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse
import com.example.spotify.data.models.network.GenreListResponse
import com.example.spotify.data.models.network.SearchResponse

/**
 * Интерфейс для преобразования ответов API из Spotify Web API
 */
interface SpotifyInfoApiMapper {

    /**
     * Получает топ треки исполнителя из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param id идентификатор исполнителя
     * @return Объект [TrackListResponse], содержащий топ треки исполнителя
     */
    suspend fun getArtistsTopTracks(accessToken: String, id: String): TrackListResponse

    /**
     * Получает аудиохарактеристики треков из API Spotify.
     *
     * @param accessToken OAuth токен доступа для аутентификации запроса
     * @param ids идентификаторы треков, разделенные запятыми, для получения аудиохарактеристик
     * @return объект [AudioFeaturesListResponse], содержащий аудиохарактеристики треков
     */
    suspend fun getTracksAudioFeatures(accessToken: String, ids: String): AudioFeaturesListResponse

    suspend fun search(accessToken: String, type: String, query: String): SearchResponse

    suspend fun searchGenres(accessToken: String): GenreListResponse

    suspend fun createPlaylist(
        accessToken: String,
        artists: String,
        genres: String,
        tracks: String
    ): TrackListResponse
}