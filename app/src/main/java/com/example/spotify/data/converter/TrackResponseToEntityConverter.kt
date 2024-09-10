package com.example.spotify.data.converter

import com.example.spotify.data.db.TrackEntity
import com.example.spotify.models.data.AlbumInfo
import com.example.spotify.models.data.net.Album
import com.example.spotify.models.data.net.TrackResponse

/**
 * Конвертер сетевой модели [TrackResponse] в модель бд [TrackEntity]
 */
class TrackResponseToEntityConverter {

    /**
     * Конвертирует [TrackResponse] в [TrackEntity]
     *
     * @param from данные для конвертации
     */
    fun convert(from: TrackResponse): TrackEntity =
        TrackEntity(
            id = from.id,
            name = from.name,
            previewUrl = from.previewUrl.orEmpty(),
            duration = from.duration,
            artistsId = from.artists.map{ it.id },
            artistsName = from.artists.map{ it.name },
            album = convertAlbum(from.album),
            isExplicit = from.isExplicit,
            isPlayable = from.isPlayable,
            popularity = from.popularity
        )

    private fun convertAlbum(from: Album): AlbumInfo =
        AlbumInfo(
            id = from.id,
            name = from.name,
            image = from.images.last().url
        )
}
