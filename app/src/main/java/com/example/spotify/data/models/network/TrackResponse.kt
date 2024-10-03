package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

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
    @SerializedName("artists") val artists: List<ShortArtistResponse>,
    @SerializedName("album") val album: AlbumResponse,
    @SerializedName("explicit") val isExplicit: Boolean,
    @SerializedName("is_playable") val isPlayable: Boolean,
    @SerializedName("popularity") val popularity: Int
)