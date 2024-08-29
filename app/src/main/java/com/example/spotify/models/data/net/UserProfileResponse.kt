package com.example.spotify.models.data.net

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("email") val email: String,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("country") val country: String,
    @SerializedName("product") val product: String
)

data class Image(
    @SerializedName("url") val url: String
)