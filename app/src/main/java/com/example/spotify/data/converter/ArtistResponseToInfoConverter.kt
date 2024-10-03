package com.example.spotify.data.converter

import com.example.spotify.data.models.network.ArtistResponse
import com.example.spotify.domain.models.ArtistInfo

/**
 * Конвертер сетевой модели [ArtistResponse] в модель базы данных [ArtistInfo]
 */
class ArtistResponseToInfoConverter {

    /**
     * Конвертирует [ArtistResponse] в [ArtistInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: ArtistResponse): ArtistInfo =
        ArtistInfo(
            id = from.id,
            name = from.name,
            popularity = from.popularity,
            genres = from.genres.joinToString { it },
            image = from.images.lastOrNull()?.url.orEmpty()
        )
}
