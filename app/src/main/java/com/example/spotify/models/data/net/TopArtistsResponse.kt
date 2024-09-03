package com.example.spotify.models.data.net

import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("items") val items: List<ArtistResponse>,
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int
)

data class ArtistResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("popularity") val popularity: Int,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("images") val images: List<Image>
)