package com.example.spotify.models.data.net

import com.google.gson.annotations.SerializedName

data class ArtistsTopTracksResponse(
    @SerializedName("tracks") val tracks: List<Track>
)