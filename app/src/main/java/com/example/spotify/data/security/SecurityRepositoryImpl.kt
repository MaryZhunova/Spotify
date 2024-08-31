package com.example.spotify.data.security

import com.example.spotify.data.security.net.ClientCredentialsApiMapper
import com.example.spotify.domain.security.SecurityRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    private val apiMapper: ClientCredentialsApiMapper,
    private val accountHelper: TokenStorage
) : SecurityRepository {

    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        val token = accountHelper.getAccessToken()
        return@withContext if (token != null) {
            token
        } else {
            val accessTokenResponse = apiMapper.getClientCredentialsToken()
            accessTokenResponse?.let {
                accountHelper.storeAccessToken(it.accessToken)
                scheduleTokenDeletion(it.expiresIn)
                it.accessToken
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun scheduleTokenDeletion(expiresIn: Int) {
        val delay = expiresIn * 1000L
        kotlinx.coroutines.GlobalScope.launch {
            delay(delay)
            accountHelper.removeAccessToken()
        }
    }
}