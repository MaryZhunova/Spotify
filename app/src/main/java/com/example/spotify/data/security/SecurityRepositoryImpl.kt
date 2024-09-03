package com.example.spotify.data.security

import com.example.spotify.data.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.data.security.net.ClientCredentialsApiMapper
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.data.AccessTokenInfo
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
//            getAccessToken() ?:
            apiMapper.getAuthToken(accessCode, redirectUri)?.also { token ->
                    tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(token))
                }?.accessToken
        }

    private fun checkAccessToken(token: AccessTokenInfo): Boolean =
        token.expiresAt < System.currentTimeMillis()

    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        val token = tokenStorage.getAccessToken() ?: return@withContext null
        return@withContext if (checkAccessToken(token)) {
        token.refreshToken?.let { refreshAccessToken(it) }
        } else {
            token.accessToken
        }
    }

    override fun clear() {
        tokenStorage.clear()
    }

    private suspend fun refreshAccessToken(refreshToken: String): String? =
        apiMapper.refreshAuthToken(refreshToken)?.also { newToken ->
            tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(newToken))
        }?.accessToken
}