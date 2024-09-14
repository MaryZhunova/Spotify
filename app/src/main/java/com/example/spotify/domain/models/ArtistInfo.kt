package com.example.spotify.domain.models

/**
 * Информация об отдельном исполнителе
 *
 * @property id уникальный идентификатор исполнителя
 * @property name имя исполнителя
 * @property popularity популярность исполнителя
 * @property genres список жанров, к которым относится исполнитель
 * @property image URL изображения исполнителя
 */
data class ArtistInfo(
    val id: String,
    val name: String,
    val popularity: Int,
    val genres: String,
    val image: String
)