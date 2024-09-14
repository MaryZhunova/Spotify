package com.example.spotify.data.auth

import com.example.spotify.data.auth.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.data.auth.network.SpotifyAuthApiMapper
import com.example.spotify.data.auth.storage.TokenStorage
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.auth.AccessTokenInfo
import com.example.spotify.utils.TimeSource
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
 * @param timeSource класс для получения текущего времени
 */
class AuthRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyAuthApiMapper,
    private val tokenStorage: TokenStorage,
    private val accessTokenResponseConverter: AccessTokenResponseToInfoConverter,
    private val timeSource: TimeSource
) : AuthRepository {

    override suspend fun obtainAccessToken(accessCode: String, redirectUri: String): String? =
        withContext(Dispatchers.IO) {
            getAccessTokenInternal() ?: apiMapper.getAuthToken(accessCode, redirectUri)?.also { token ->
                    tokenStorage.storeAccessToken(accessTokenResponseConverter.convert(token))
                }?.accessToken
        }

    private fun isAccessTokenExpired(token: AccessTokenInfo): Boolean =
        token.expiresAt < timeSource.getCurrentTime()

    override suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        getAccessTokenInternal() ?: throw NullAccessTokenException
    }

    override fun clear() {
        tokenStorage.clear()
    }

    private suspend fun getAccessTokenInternal(): String? {
        val token = tokenStorage.getAccessToken() ?: return null
        return if (isAccessTokenExpired(token)) {
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