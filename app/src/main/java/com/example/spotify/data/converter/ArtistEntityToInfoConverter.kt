package com.example.spotify.data.converter

import com.example.spotify.data.models.db.ArtistEntity
import com.example.spotify.domain.models.ArtistInfo

/**
 * Конвертер модели базы данных [ArtistEntity] в дата модель [ArtistInfo]
 */
class ArtistEntityToInfoConverter {

    /**
     * Конвертирует [ArtistEntity] в [ArtistInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: ArtistEntity): ArtistInfo =
        ArtistInfo(
            id = from.id,
            name = from.name,
            popularity = from.popularity,
            genres = from.genres.joinToString { it },
            image = from.bigImage.takeIf { it.isNotBlank() } ?: from.smallImage
        )
}
