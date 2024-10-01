package com.example.spotify.data.converter

import com.example.spotify.data.db.TrackDao
import com.example.spotify.domain.models.AlbumInfo
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.domain.models.TrackInfo

/**
 * Конвертер сетевой модели [TrackResponse] в доменную модель [TrackInfo]
 *
 * @constructor
 * @param trackDao DAO для работы с таблицей треков в базе данных
 */
class TrackResponseToInfoConverter(private val trackDao: TrackDao) {

    /**
     * Конвертирует [TrackResponse] в [TrackInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: TrackResponse): TrackInfo =
        TrackInfo(
            id = from.id,
            name = from.name,
            previewUrl = from.previewUrl,
            duration = from.duration,
            artists = from.artists.joinToString { it.name },
            album = AlbumInfo(
                id = from.album.id,
                name = from.album.name,
                image = from.album.images.last().url
            ),
            isExplicit = from.isExplicit,
            isPlayable = from.isPlayable,
            popularity = from.popularity,
            isFavorite = trackDao.isTrackInDatabase(from.name)
        )
}
