package com.example.spotify.data.auth.network

import com.example.spotify.data.auth.models.AccessTokenResponse

/**
 * Интерфейс для преобразования ответов API аутентификации Spotify.
 */
interface SpotifyAuthApiMapper {

    /**
     * Получает токен доступа, используя авторизационный код и URI перенаправления
     *
     * @param accessCode авторизационный код, полученный в процессе авторизации пользователя
     * @param redirectUri URI для перенаправления после получения токена доступа
     * @return [AccessTokenResponse] с информацией о токене доступа или `null`, если запрос не удался
     */
    fun getAuthToken(accessCode: String, redirectUri: String): AccessTokenResponse?

    /**
     * Обновляет токен доступа, используя токен обновления
     *
     * @param refreshToken токен обновления, используемый для получения нового токена доступа
     * @return [AccessTokenResponse] с новым токеном доступа или `null`, если запрос не удался
     */
    fun refreshAuthToken(refreshToken: String): AccessTokenResponse?
}