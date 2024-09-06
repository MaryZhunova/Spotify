package com.example.spotify.data.security

import com.example.spotify.data.security.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.data.security.net.SpotifyAuthApiMapper
import com.example.spotify.domain.security.AuthRepository
import com.example.spotify.models.data.security.AccessTokenInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Репозиторий для хранения токенов доступа
 *
 * @constructor
 * @param apiMapper маппер для работы с API авторизации Spotify
 * @param tokenStorage хранилище для безопасного хранения токенов
 * @param accessTokenResponseConverter конвертер для преобразования ответа на запрос токена доступа
 */
class AuthRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyAuthApiMapper,
    private val tokenStorage: TokenStorage,
    private val accessTokenResponseConverter: AccessTokenResponseToInfoConverter
) : AuthRepository {

    override suspend fun obtainAccessToken(accessCode: String, redirectUri: String): String? =
        withContext(Dispatchers.IO) {
            getAccessTokenInternal() ?: apiMapper.getAuthToken(accessCode, redirectUri)?.also { token ->
                    tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(token))
                }?.accessToken
        }

    private fun checkAccessToken(token: AccessTokenInfo): Boolean =
        token.expiresAt < System.currentTimeMillis()

    override suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        getAccessTokenInternal() ?: throw NullAccessTokenException
    }

    override fun clear() {
        tokenStorage.clear()
    }

    private suspend fun getAccessTokenInternal(): String? {
        val token = tokenStorage.getAccessToken() ?: return null
        return if (checkAccessToken(token)) {
            token.refreshToken?.let { refreshAccessToken(it) }
        } else {
            token.accessToken
        }
    }

    private suspend fun refreshAccessToken(refreshToken: String): String? =
        apiMapper.refreshAuthToken(refreshToken)?.also { newToken ->
            tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(newToken))
        }?.accessToken
}

data object NullAccessTokenException: NullPointerException()