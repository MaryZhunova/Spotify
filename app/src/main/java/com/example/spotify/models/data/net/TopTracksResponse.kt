package com.example.spotify.models.data.net

import com.google.gson.annotations.SerializedName

/**
 * Ответ от API, содержащий информацию о топ-треках
 *
 * @property items список треков, полученных в ответе
 * @property href URL для получения следующего набора данных
 * @property limit количество треков в текущем ответе
 * @property next URL для получения следующей страницы данных, если таковая имеется
 * @property offset смещение относительно общего списка треков
 * @property previous URL для получения предыдущей страницы данных, если таковая имеется
 * @property total общее количество треков, соответствующих запросу
 */
data class TopTracksResponse(
    @SerializedName("items") val items: List<TrackResponse>,
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int,
    @SerializedName("previous") val previous: String,
    @SerializedName("total") val total: Int
)

/**
 * Информация о треке
 *
 * @property id уникальный идентификатор трека
 * @property name название трека
 * @property artists список исполнителей, исполняющих трек
 * @property album альбом, в который входит трек
 * @property popularity популярность трека
 */
data class TrackResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("duration_ms") val duration: Int,
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("album") val album: Album,
    @SerializedName("explicit") val isExplicit: Boolean,
    @SerializedName("is_playable") val isPlayable: Boolean,
    @SerializedName("popularity") val popularity: Int
)

/**
 * Информация об исполнителе
 *
 * @property id уникальный идентификатор исполнителя
 * @property name имя исполнителя
 */
data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

/**
 * Информация об альбоме
 *
 * @property id уникальный идентификатор альбома
 * @property name название альбома
 * @property images список изображений альбома
 */
data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String
)