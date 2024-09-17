package com.example.spotify.data.auth.network

import com.example.spotify.data.auth.models.AccessTokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Интерфейс для взаимодействия с API аутентификации Spotify
 */
interface SpotifyAuthApiService {

    /**
     * Обменивает авторизационный код на токен доступа
     *
     * @param authorization header авторизации, содержащий базовую аутентификацию
     * @param grantType тип предоставляемого гранта (в этом случае "authorization_code")
     * @param code код, полученный в процессе авторизации пользователя
     * @param redirectUri URI для перенаправления после успешного получения токена
     * @return [Response] для получения [AccessTokenResponse], содержащего токен доступа
     */
    @FormUrlEncoded
    @POST("api/token")
    suspend fun exchangeCodeForToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Response<AccessTokenResponse>

    /**
     * Обновляет токен доступа с использованием токена обновления
     *
     * @param authorization header авторизации, содержащий базовую аутентификацию
     * @param grantType тип предоставляемого гранта (в этом случае "refresh_token").
     * @param token токен обновления, используемый для получения нового токена доступа
     * @return [Response] для получения [AccessTokenResponse], содержащего новый токен доступа
     */
    @FormUrlEncoded
    @POST("api/token")
    suspend fun refreshAuthToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") token: String,
    ): Response<AccessTokenResponse>
}