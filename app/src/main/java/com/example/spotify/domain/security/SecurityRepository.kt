package com.example.spotify.domain.security

interface SecurityRepository {

    suspend fun obtainAccessToken(accessCode: String, redirectUri: String): String?

    suspend fun getAccessToken(): String?

    fun clear()
}