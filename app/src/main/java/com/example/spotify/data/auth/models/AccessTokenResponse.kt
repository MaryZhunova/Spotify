package com.example.spotify.data.auth.models

import com.google.gson.annotations.SerializedName

/**
 * Ответ от API, содержащий информацию о токене доступа
 *
 * @property accessToken токен доступа, который используется для аутентификации запросов к API
 * @property tokenType тип токена доступа (например, "Bearer")
 * @property expiresIn время в секундах до истечения срока действия токена
 * @property refreshToken опциональный токен для обновления текущего токена доступа, если он истек
 * @property scope области доступа, для которых выданы права (например, "user-read-private user-read-email")
 */
data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("scope") val scope: String
)