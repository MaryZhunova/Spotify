package com.example.spotify.data.network.api

import com.example.spotify.data.models.network.ArtistsTopTracksResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse
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
     * @return [Call] для получения [ArtistsTopTracksResponse], содержащего информацию о топ треках исполнителя
     */
    @GET("v1/artists/{id}/top-tracks")
    fun getArtistsTopTracks(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ArtistsTopTracksResponse>

    /**
     * Получает аудио информацию треков
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param ids идентификаторы треков
     * @return [Call] для получения [ArtistsTopTracksResponse], содержащего информацию о топ треках исполнителя
     */
    @GET("v1/audio-features")
    fun getTracksAudioFeatures(
        @Header("Authorization") token: String,
        @Query("ids") ids: String,
    ): Call<AudioFeaturesListResponse>
}