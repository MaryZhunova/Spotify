package com.example.spotify.models.data

data class UserProfileInfo(
    val id: String,
    val displayName: String,
    val email: String,
    val image: String?,
    val country: String,
    val product: String
)