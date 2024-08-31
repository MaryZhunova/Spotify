package com.example.spotify.domain.security

interface SecurityRepository {

    suspend fun getAccessToken(): String?

}