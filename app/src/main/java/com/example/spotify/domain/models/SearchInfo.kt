package com.example.spotify.domain.models

data class SearchInfo(
    val tracks: List<TrackInfo> = emptyList(),
    val artists: List<ArtistInfo> = emptyList(),
)