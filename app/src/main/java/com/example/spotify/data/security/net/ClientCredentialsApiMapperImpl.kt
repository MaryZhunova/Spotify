package com.example.spotify.data.security.net

import com.example.spotify.BuildConfig
import com.example.spotify.models.data.net.AccessTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import retrofit2.await
import javax.inject.Inject

class ClientCredentialsApiMapperImpl @Inject constructor(
    private val apiService: ClientCredentialsApiService
) : ClientCredentialsApiMapper {

    override suspend fun getClientCredentialsToken(): AccessTokenResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val clientId = BuildConfig.CLIENT_ID
                val clientSecret = BuildConfig.CLIENT_SECRET

                val authHeader = Credentials.basic(clientId, clientSecret)
                val request = apiService.getClientCredentialsToken(authHeader)
                val response = request.await()
                response
            } catch (e: Exception) {
                null
            }
        }
    }
}