package com.example.spotify.models.data

/**
 * Профиль пользователя
 *
 * @property id уникальный идентификатор пользователя
 * @property displayName имя пользователя
 * @property email адрес электронной почты пользователя
 * @property image URL изображения профиля пользователя, если оно доступно
 * @property country старана пользователя
 * @property product продукт Spotify, который использует пользователь
 */
data class UserProfileInfo(
    val id: String,
    val displayName: String,
    val email: String,
    val image: String?,
    val country: String,
    val product: String
)