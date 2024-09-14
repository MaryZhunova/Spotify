package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Ответ от API, содержащий информацию о профиле пользователя
 *
 * @property id уникальный идентификатор пользователя
 * @property displayName имя пользователя
 * @property email адрес электронной почты пользователя
 * @property images список изображений профиля пользователя
 * @property country код страны пользователя
 * @property product продукт Spotify, который использует пользователь
 */
data class UserProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("email") val email: String,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("country") val country: String,
    @SerializedName("product") val product: String
)

/**
 * Изображение
 *
 * @property url URL изображения
 */
data class Image(
    @SerializedName("url") val url: String
)
