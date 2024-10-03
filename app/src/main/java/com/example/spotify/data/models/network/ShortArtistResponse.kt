package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Информация об исполнителе
 *
 * @property id уникальный идентификатор исполнителя
 * @property name имя исполнителя
 */
data class ShortArtistResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)
