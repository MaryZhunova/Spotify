package com.example.spotify.domain.security

interface SecurityRepository {

    suspend fun getAuthAccessToken(accessCode: String, redirectUri: String): String?
}