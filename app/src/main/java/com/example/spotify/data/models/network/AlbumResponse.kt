package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Информация об альбоме
 *
 * @property id уникальный идентификатор альбома
 * @property name название альбома
 * @property images список изображений альбома
 */
data class AlbumResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<ImageResponse>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String
)