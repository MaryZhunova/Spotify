package com.example.spotify.data.net

import com.example.spotify.models.data.net.ArtistsTopTracksResponse
import com.example.spotify.models.data.net.TopArtistsResponse
import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Интерфейс для взаимодействия с API Spotify
 */
interface SpotifyStatsApiService {

    /**
     * Получает текущий профиль пользователя
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @return [Call] для получения [UserProfileResponse], содержащего информацию о профиле пользователя
     */
    @GET("v1/me")
    fun getCurrentUserProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>

    /**
     * Получает топ треки пользователя
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param timeRange временной интервал для топ треков (например, "short_term", "medium_term", "long_term")
     * @param limit количество топ треков, которые нужно вернуть
     * @param offset смещение для пагинации
     * @return [Call] для получения [TopTracksResponse], содержащего информацию о топ треках пользователя
     */
    @GET("v1/me/top/tracks")
    fun getTopTracks(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "short_term",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): Call<TopTracksResponse>

    /**
     * Получает следующую страницу топ треков
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param url URL для получения следующей страницы топ треков
     * @return [Call] для получения [TopTracksResponse], содержащего следующую страницу информации о топ треках
     */
    @GET
    fun getTopTracksNextPage(
        @Header("Authorization") token: String,
        @Url url: String
    ): Call<TopTracksResponse>

    /**
     * Получает топ исполнителей пользователя
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param timeRange временной интервал для топ исполнителей (например, "short_term", "medium_term", "long_term")
     * @param limit количество топ исполнителей, которые нужно вернуть
     * @param offset смещение для пагинации
     * @return [Call] для получения [TopArtistsResponse], содержащего информацию о топ исполнителях пользователя
     */
    @GET("v1/me/top/artists")
    fun getTopArtists(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "short_term",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): Call<TopArtistsResponse>

    /**
     * Получает следующую страницу топ исполнителей
     *
     * @param token OAuth токен доступа для аутентификации запроса
     * @param url URL для получения следующей страницы топ исполнителей
     * @return [Call] для получения [TopArtistsResponse], содержащего следующую страницу информации о топ исполнителях
     */
    @GET
    fun getTopArtistsNextPage(
        @Header("Authorization") token: String,
        @Url url: String
    ): Call<TopArtistsResponse>

    @GET("v1/artists/{id}/top-tracks")
    fun getArtistsTopTracks(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ArtistsTopTracksResponse>
}