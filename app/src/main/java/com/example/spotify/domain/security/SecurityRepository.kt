package com.example.spotify.domain.security

interface SecurityRepository {

    suspend fun obtainAccessToken(accessCode: String, redirectUri: String): String?

    fun getAccessToken(): String?

    suspend fun refreshAccessToken(): String?
}