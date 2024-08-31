package com.example.spotify.data.security.net

import com.example.spotify.models.data.net.AccessTokenResponse

interface ClientCredentialsApiMapper {

    suspend fun getClientCredentialsToken(): AccessTokenResponse?
}