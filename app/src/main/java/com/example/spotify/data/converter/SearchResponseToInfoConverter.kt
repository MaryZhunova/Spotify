package com.example.spotify.data.converter

import com.example.spotify.data.models.network.AudioFeaturesResponse
import com.example.spotify.data.models.network.SearchResponse
import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.SearchInfo

/**
 * Конвертер сетевой модели [AudioFeaturesResponse] в модель [AudioFeaturesInfo]
 */
class SearchResponseToInfoConverter(
    private val trackInfoConverter: TrackResponseToInfoConverter,
    private val artistInfoConverter: ArtistResponseToInfoConverter
) {

    /**
     * Конвертирует [AudioFeaturesResponse] в [AudioFeaturesInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: SearchResponse): SearchInfo =
        SearchInfo(
            tracks = from.tracks?.items?.map(trackInfoConverter::convert) ?: emptyList(),
            artists = from.artists?.items?.map(artistInfoConverter::convert) ?: emptyList()
        )

}