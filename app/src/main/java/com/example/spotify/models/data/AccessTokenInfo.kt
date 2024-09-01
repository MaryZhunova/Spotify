package com.example.spotify.models.data

data class AccessTokenInfo(
    val accessToken: String,
    val tokenType: String,
    val expiresAt: Long,
    val refreshToken: String,
)