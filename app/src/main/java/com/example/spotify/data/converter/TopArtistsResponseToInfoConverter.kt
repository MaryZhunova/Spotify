package com.example.spotify.data.converter

import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.data.TopArtistsInfo
import com.example.spotify.models.data.net.ArtistResponse
import com.example.spotify.models.data.net.TopArtistsResponse

class TopArtistsResponseToInfoConverter {
    fun convert(from: TopArtistsResponse): TopArtistsInfo =
        TopArtistsInfo(
            items = from.items.map { convertArtist(it) },
            next = from.next
        )

    private fun convertArtist(from: ArtistResponse): ArtistInfo =
        ArtistInfo(
            id = from.id,
            name = from.name,
            popularity = from.popularity,
            genres = from.genres.joinToString { it },
            image = from.images.last().url
        )
}
