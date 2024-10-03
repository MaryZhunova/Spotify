package com.example.spotify.data.network.api

import com.example.spotify.data.models.network.TrackListResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse
import com.example.spotify.data.models.network.GenreListResponse
import com.example.spotify.data.models.network.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Интерфейс для взаимодействия с API Spotify
 */
interface SpotifyInfoApiService {

    /**
     * Получает топ треки исполнителя
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param id идентификатор исполнителя
     * @return [Call] для получения [TrackListResponse], содержащего информацию о топ треках исполнителя
     */
    @GET("v1/artists/{id}/top-tracks")
    fun getArtistsTopTracks(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<TrackListResponse>

    /**
     * Получает аудио информацию треков
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param ids идентификаторы треков
     * @return [Call] для получения [TrackListResponse], содержащего информацию о топ треках исполнителя
     */
    @GET("v1/audio-features")
    fun getTracksAudioFeatures(
        @Header("Authorization") token: String,
        @Query("ids") ids: String,
    ): Call<AudioFeaturesListResponse>

    @GET("v1/search")
    fun search(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int = 20
    ): Call<SearchResponse>

    @GET("v1/recommendations/available-genre-seeds")
    fun searchGenres(
        @Header("Authorization") token: String
    ): Call<GenreListResponse>

    @GET("v1/recommendations")
    fun createPlaylist(
        @Header("Authorization") token: String,
        @Query("seed_artists") artists: String,
        @Query("seed_genres") genres: String,
        @Query("seed_tracks") tracks: String,
        @Query("limit") limit: Int = 20
    ): Call<TrackListResponse>
}