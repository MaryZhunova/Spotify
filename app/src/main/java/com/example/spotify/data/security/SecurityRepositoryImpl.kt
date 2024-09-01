package com.example.spotify.data.security

import com.example.spotify.data.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.data.security.net.ClientCredentialsApiMapper
import com.example.spotify.domain.security.SecurityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    private val apiMapper: ClientCredentialsApiMapper,
    private val tokenStorage: TokenStorage,
    private val accessTokenResponseConverter: AccessTokenResponseToInfoConverter
) : SecurityRepository {

    override suspend fun obtainAccessToken(accessCode: String, redirectUri: String): String? =
        withContext(Dispatchers.IO) {
            getAccessToken() ?: apiMapper.getAuthToken(accessCode, redirectUri)?.also { token ->
                    tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(token))
                }?.accessToken
        }

    override fun getAccessToken(): String? = tokenStorage.getAccessToken()?.accessToken

    override suspend fun refreshAccessToken(): String? = withContext(Dispatchers.IO) {
        val token = tokenStorage.getAccessToken() ?: return@withContext null
        if (token.expiresAt < System.currentTimeMillis()) {
            apiMapper.refreshAuthToken(token.refreshToken)?.also { newToken ->
                tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(newToken))
            }?.accessToken
        } else {
            null
        }

    }
}