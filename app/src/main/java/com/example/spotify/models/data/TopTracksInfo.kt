package com.example.spotify.models.data

data class TopTracksInfo(
val items: List<TrackInfo>,
val next: String?
)

data class TrackInfo(
    val id: String,
    val name: String,
    val artists: List<ArtistInfo>,
    val album: AlbumInfo,
    val popularity: Int
)

data class ArtistInfo(
    val id: String,
    val name: String
)

data class AlbumInfo(
    val id: String,
    val name: String,
   val image: String
)