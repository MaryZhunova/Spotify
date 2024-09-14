package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Ответ от API, содержащий информацию о топ исполнителях
 *
 * @property items список исполнителей, полученных в ответе
 * @property href URL для получения следующего набора данных
 * @property limit количество исполнителей в текущем ответе
 * @property next URL для получения следующей страницы данных, если таковая имеется
 * @property offset смещение относительно общего списка исполнителей
 * @property previous URL для получения предыдущей страницы данных, если таковая имеется
 * @property total общее количество исполнителей
 */
data class TopArtistsResponse(
    @SerializedName("items") val items: List<ArtistResponse>,
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int
)

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
    @SerializedName("images") val images: List<Image>
)