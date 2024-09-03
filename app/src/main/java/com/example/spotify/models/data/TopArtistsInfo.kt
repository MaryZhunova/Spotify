package com.example.spotify.models.data

data class TopArtistsInfo(
    val items: List<ArtistInfo>,
    val next: String?
)

data class ArtistInfo(
    val id: String,
    val name: String,
    val popularity: Int,
    val genres: String,
    val image: String
)