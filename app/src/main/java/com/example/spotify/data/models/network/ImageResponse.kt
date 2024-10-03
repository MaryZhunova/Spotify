package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Изображение
 *
 * @property url URL изображения
 */
data class ImageResponse(
    @SerializedName("url") val url: String
)