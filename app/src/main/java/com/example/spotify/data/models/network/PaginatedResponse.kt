package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 *
 * @property items список элементов, полученных в ответе
 * @property href URL для получения следующего набора данных
 * @property limit количество элементов в текущем ответе
 * @property next URL для получения следующей страницы данных, если таковая имеется
 * @property offset смещение относительно общего списка элементов
 * @property previous URL для получения предыдущей страницы данных, если таковая имеется
 * @property total общее количество элементов, соответствующих запросу
 */
data class PaginatedResponse<T>(
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int,
    @SerializedName("items") val items: List<T>
)