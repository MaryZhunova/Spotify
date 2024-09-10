package com.example.spotify.models.data

/**
 * Информация о треке
 *
 * @property id уникальный идентификатор трека
 * @property name название трека
 * @property previewUrl URL для предварительного прослушивания трека. Может быть `null`,
 * если предварительный прослушивание недоступно
 * @property duration продолжительность трека в секундах
 * @property artists список исполнителей, исполняющих трек, представленный в виде строки, разделенной запятой
 * @property album информация об альбоме, в который входит трек
 * @property isExplicit флаг, указывающий, содержит ли трек неприемлемый контент (нецензурные слова и т.п.)
 * @property isPlayable флаг, указывающий, можно ли воспроизвести трек
 * @property popularity популярность трека, представлена в виде числа (чем выше число, тем популярнее трек)
 * @property isFavorite входит ли в список любимых треков текущего пользователя
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