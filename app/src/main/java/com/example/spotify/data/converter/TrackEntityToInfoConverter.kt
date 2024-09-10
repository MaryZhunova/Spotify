package com.example.spotify.data.converter

import com.example.spotify.data.db.TrackEntity
import com.example.spotify.models.data.TrackInfo

/**
 * Конвертер модели бд [TrackEntity] в дата модель [TrackInfo]
 */
class TrackEntityToInfoConverter {

    /**
     * Конвертирует [TrackEntity] в [TrackInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: TrackEntity): TrackInfo =
        TrackInfo(
            id = from.id,
            name = from.name,
            previewUrl = from.previewUrl,
            duration = from.duration,
            artists = from.artistsName.joinToString(","),
            album = from.album,
            isExplicit = from.isExplicit,
            isPlayable = from.isPlayable,
            popularity = from.popularity
        )
}
