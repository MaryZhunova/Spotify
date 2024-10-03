package com.example.spotify.domain.models

/**
 * Информация об альбоме
 *
 * @property id уникальный идентификатор альбома
 * @property name название альбома
 * @property image URL изображения альбома
 */
data class AlbumInfo(
    val id: String,
    val name: String,
    val image: String
)