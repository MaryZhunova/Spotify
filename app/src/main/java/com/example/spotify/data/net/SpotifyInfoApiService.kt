package com.example.spotify.data.net

import com.example.spotify.models.data.net.ArtistsTopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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
}