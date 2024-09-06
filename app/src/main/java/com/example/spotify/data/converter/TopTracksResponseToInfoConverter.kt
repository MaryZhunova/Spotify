package com.example.spotify.data.converter

import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.net.TopTracksResponse
import javax.inject.Inject

/**
 * Конвертер сетевой модели [TopTracksResponse] в дата модель [TopTracksInfo]
 */
class TopTracksResponseToInfoConverter @Inject constructor(
    private val trackConverter: TrackResponseToInfoConverter
) {

    /**
     * Конвертирует [TopTracksResponse] в [TopTracksInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: TopTracksResponse): TopTracksInfo =
        TopTracksInfo(
            items = from.items.map { trackConverter.convert(it) },
            next = from.next
        )
}
