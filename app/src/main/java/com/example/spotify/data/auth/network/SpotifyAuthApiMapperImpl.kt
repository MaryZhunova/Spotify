package com.example.spotify.data.auth.network

import com.example.spotify.BuildConfig
import com.example.spotify.data.auth.models.AccessTokenResponse
import okhttp3.Credentials
import javax.inject.Inject

/**
 * Реализация интерфейса [SpotifyAuthApiMapper],
 * которая использует [SpotifyAuthApiService] для выполнения запросов
 *
 * @constructor
 * @param apiService сервис для взаимодействия с API аутентификации Spotify
 */
class SpotifyAuthApiMapperImpl @Inject constructor(
    private val apiService: SpotifyAuthApiService
) : SpotifyAuthApiMapper {

    override fun getAuthToken(
        accessCode: String, redirectUri: String
    ): AccessTokenResponse? {
        val clientId = BuildConfig.CLIENT_ID
        val clientSecret = BuildConfig.CLIENT_SECRET

        val authHeader = Credentials.basic(clientId, clientSecret)
        val response = apiService.exchangeCodeForToken(
            authorization = authHeader, code = accessCode, redirectUri = redirectUri
        ).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override fun refreshAuthToken(refreshToken: String): AccessTokenResponse? {
        val clientId = BuildConfig.CLIENT_ID
        val clientSecret = BuildConfig.CLIENT_SECRET

        val authHeader = Credentials.basic(clientId, clientSecret)
        val response =
            apiService.refreshAuthToken(authorization = authHeader, token = refreshToken).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}