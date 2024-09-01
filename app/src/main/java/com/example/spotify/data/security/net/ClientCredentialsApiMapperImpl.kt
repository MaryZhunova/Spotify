package com.example.spotify.data.security.net

import com.example.spotify.BuildConfig
import com.example.spotify.models.data.net.AccessTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import javax.inject.Inject

class ClientCredentialsApiMapperImpl @Inject constructor(
    private val apiService: ClientCredentialsApiService
) : ClientCredentialsApiMapper {

    override suspend fun getAuthToken(
        accessCode: String, redirectUri: String
    ): AccessTokenResponse? = withContext(Dispatchers.IO) {
        val clientId = BuildConfig.CLIENT_ID
        val clientSecret = BuildConfig.CLIENT_SECRET

        val authHeader = Credentials.basic(clientId, clientSecret)
        val response = apiService.exchangeCodeForToken(
            authorization = authHeader, code = accessCode, redirectUri = redirectUri
        )
        return@withContext if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun refreshAuthToken(refreshToken: String): AccessTokenResponse? =
        withContext(Dispatchers.IO) {
            val response = apiService.refreshAuthToken(token = refreshToken)
            return@withContext if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
}