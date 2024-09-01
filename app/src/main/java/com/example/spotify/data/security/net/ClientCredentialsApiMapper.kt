package com.example.spotify.data.security.net

import com.example.spotify.models.data.net.AccessTokenResponse

interface ClientCredentialsApiMapper {

    suspend fun getAuthToken(accessCode: String, redirectUri: String): AccessTokenResponse?

    suspend fun refreshAuthToken(refreshToken: String): AccessTokenResponse?
}