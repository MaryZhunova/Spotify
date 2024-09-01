package com.example.spotify.data.converter

import com.example.spotify.models.data.AlbumInfo
import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.models.data.net.Album
import com.example.spotify.models.data.net.Artist
import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.Track

class TopTracksResponseToInfoConverter {
    fun convert(from: TopTracksResponse): TopTracksInfo =
        TopTracksInfo(
            items = from.items.map { convertTrack(it) },
            next = from.next
        )

    private fun convertTrack(from: Track): TrackInfo =
        TrackInfo(
            id = from.id,
            name = from.name,
            artists = from.artists.map { convertArtist(it) },
            album = convertAlbum(from.album),
            popularity = from.popularity
        )

    private fun convertArtist(from: Artist): ArtistInfo =
        ArtistInfo(
            id = from.id,
            name = from.name
        )

    private fun convertAlbum(from: Album): AlbumInfo =
        AlbumInfo(
            id = from.id,
            name = from.name,
            image = from.images.last().url
        )
}
