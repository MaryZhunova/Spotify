package com.example.spotify.data.security

import com.example.spotify.data.security.net.ClientCredentialsApiMapper
import com.example.spotify.domain.security.SecurityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    private val apiMapper: ClientCredentialsApiMapper,
    private val accountHelper: TokenStorage
) : SecurityRepository {

    override suspend fun getAuthAccessToken(accessCode: String, redirectUri: String): String? =
        withContext(Dispatchers.IO) {
            accountHelper.getAccessToken()
                ?: apiMapper.getAuthToken(accessCode, redirectUri)?.accessToken?.also { token ->
                    accountHelper.storeAccessToken(token)
                }
        }
}