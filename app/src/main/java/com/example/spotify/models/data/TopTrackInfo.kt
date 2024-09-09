package com.example.spotify.models.data

/**
 * Информация о треке
 *
 * @property id уникальный идентификатор трека
 * @property name название трека
 * @property artists список исполнителей, исполняющих трек (в виде строки, разделенные запятой)
 * @property album альбом, в который входит трек
 * @property popularity популярность трека
 */
data class TopTrackInfo(
    val id: String,
    val name: String,
    val previewUrl: String?,
    val duration: Int,
    val artists: String,
    val album: AlbumInfo,
    val isExplicit: Boolean,
    val isPlayable: Boolean,
    val popularity: Int,
    val isFavorite: Boolean
)