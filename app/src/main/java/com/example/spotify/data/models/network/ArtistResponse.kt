package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Информация об отдельном исполнителе
 *
 * @property id уникальный идентификатор исполнителя
 * @property name имя исполнителя
 * @property popularity популярность исполнителя
 * @property genres список жанров, к которым относится исполнитель
 * @property images список изображений исполнителя
 */
data class ArtistResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("images") val images: List<ImageResponse>
)