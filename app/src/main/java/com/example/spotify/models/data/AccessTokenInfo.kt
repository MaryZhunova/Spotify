package com.example.spotify.models.data

/**
 * Информация о токене доступа
 *
 * @property accessToken токен доступа, который используется для аутентификации запросов к API
 * @property tokenType тип токена доступа (например, "Bearer")
 * @property expiresAt время в которое истекает действие токена (в милисекундах)
 * @property refreshToken опциональный токен для обновления текущего токена доступа, если он истек
 */
data class AccessTokenInfo(
    val accessToken: String,
    val tokenType: String,
    val expiresAt: Long,
    val refreshToken: String?,
)