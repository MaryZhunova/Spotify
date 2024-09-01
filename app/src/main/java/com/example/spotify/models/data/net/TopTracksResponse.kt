package com.example.spotify.models.data.net

import com.google.gson.annotations.SerializedName

data class TopTracksResponse(
    @SerializedName("items") val items: List<Track>,
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int,
    @SerializedName("previous") val previous: String,
    @SerializedName("total") val total: Int,
)

data class Track(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("artists") val artists: List<Artist>,
    @SerializedName("album") val album: Album,
    @SerializedName("popularity") val popularity: Int
)

data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: List<Image>
)