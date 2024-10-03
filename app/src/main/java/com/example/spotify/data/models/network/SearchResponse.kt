package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("tracks") val tracks: PaginatedResponse<TrackResponse>? = null,
    @SerializedName("artists") val artists: PaginatedResponse<ArtistResponse>? = null
)
