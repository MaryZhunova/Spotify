package com.example.spotify.domain.models

data class GenreInfo(
    val genre: String,
    val numberOfArtists: Int,
    val artistNames: List<String>
)