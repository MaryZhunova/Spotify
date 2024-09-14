package com.example.spotify.data.auth

import com.example.spotify.data.auth.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.data.auth.net.SpotifyAuthApiMapper
import com.example.spotify.models.data.auth.AccessTokenInfo
import com.example.spotify.models.data.auth.net.AccessTokenResponse
import com.example.spotify.utils.TimeSource
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Тесты [AuthRepositoryImpl]
 */
@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    private val apiMapper: SpotifyAuthApiMapper = mockk()
    private val tokenStorage: TokenStorage = mockk()
    private val accessTokenResponseConverter: AccessTokenResponseToInfoConverter = mockk()
    private val timeSource: TimeSource = mockk()

    private val authRepository = AuthRepositoryImpl(
        apiMapper,
        tokenStorage,
        accessTokenResponseConverter,
        timeSource
    )

    @Test
    fun `obtainAccessToken when token is not null`() = runTest {

        val accessTokenInfo = AccessTokenInfo(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3600 * 1000
        )

        every { tokenStorage.getAccessToken() } returns accessTokenInfo
        every { timeSource.getCurrentTime() } returns 3500 * 1000

        val result = authRepository.obtainAccessToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isEqualTo("access_token")
        coVerifySequence {
            tokenStorage.getAccessToken()
            timeSource.getCurrentTime()
        }
        coVerify(inverse = true) {
            apiMapper.getAuthToken(any(), any())
            accessTokenResponseConverter.convert(any())
            tokenStorage.storeAccessToken(any())
        }
    }

    @Test
    fun `obtainAccessToken when token is null`() = runTest {

        val accessTokenResponse = AccessTokenResponse(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "streaming"
        )
        val accessTokenInfo = AccessTokenInfo(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3600 * 1000
        )

        every { tokenStorage.getAccessToken() } returns null
        coEvery { apiMapper.getAuthToken(ACCESS_CODE, REDIRECT_URI) } returns accessTokenResponse
        every { accessTokenResponseConverter.convert(accessTokenResponse) } returns accessTokenInfo
        every { tokenStorage.storeAccessToken(accessTokenInfo) } just Runs

        val result = authRepository.obtainAccessToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isEqualTo("access_token")
        coVerifySequence {
            tokenStorage.getAccessToken()
            apiMapper.getAuthToken(ACCESS_CODE, REDIRECT_URI)
            accessTokenResponseConverter.convert(accessTokenResponse)
            tokenStorage.storeAccessToken(accessTokenInfo)
        }
    }

    @Test
    fun `obtainAccessToken when token is null and new token null`() = runTest {

        every { tokenStorage.getAccessToken() } returns null
        coEvery { apiMapper.getAuthToken(ACCESS_CODE, REDIRECT_URI) } returns null

        val result = authRepository.obtainAccessToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isNull()
        coVerifySequence {
            tokenStorage.getAccessToken()
            apiMapper.getAuthToken(ACCESS_CODE, REDIRECT_URI)
        }
        verify(inverse = true) {
            accessTokenResponseConverter.convert(any())
            tokenStorage.storeAccessToken(any())
        }
    }

    @Test
    fun `obtainAccessToken when token is not null but expired`() = runTest {

        val accessTokenResponse = AccessTokenResponse(
            accessToken = "new_access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3900,
            scope = "streaming"
        )
        val accessTokenInfo = AccessTokenInfo(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3600 * 1000
        )
        val newAccessTokenInfo = AccessTokenInfo(
            accessToken = "new_access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3900 * 1000
        )

        every { tokenStorage.getAccessToken() } returns accessTokenInfo
        every { timeSource.getCurrentTime() } returns 3800 * 1000
        coEvery { apiMapper.refreshAuthToken("refresh_token") } returns accessTokenResponse
        every { accessTokenResponseConverter.convert(accessTokenResponse) } returns newAccessTokenInfo
        every { tokenStorage.storeAccessToken(newAccessTokenInfo) } just Runs

        val result = authRepository.obtainAccessToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isEqualTo("new_access_token")
        coVerifySequence {
            tokenStorage.getAccessToken()
            timeSource.getCurrentTime()
            apiMapper.refreshAuthToken("refresh_token")
            accessTokenResponseConverter.convert(accessTokenResponse)
            tokenStorage.storeAccessToken(newAccessTokenInfo)
        }
    }

    @Test
    fun `obtainAccessToken when token is expired and new refresh token is null`() = runTest {

        val accessTokenResponse = AccessTokenResponse(
            accessToken = "new_access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3900,
            scope = "streaming"
        )
        val accessTokenInfo = AccessTokenInfo(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3600 * 1000
        )
        val newAccessTokenInfo = AccessTokenInfo(
            accessToken = "new_access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3900 * 1000
        )

        every { tokenStorage.getAccessToken() } returns accessTokenInfo
        every { timeSource.getCurrentTime() } returns 3800 * 1000
        coEvery { apiMapper.refreshAuthToken("refresh_token") } returns null
        coEvery { apiMapper.getAuthToken(ACCESS_CODE, REDIRECT_URI) } returns accessTokenResponse
        every { accessTokenResponseConverter.convert(accessTokenResponse) } returns newAccessTokenInfo
        every { tokenStorage.storeAccessToken(newAccessTokenInfo) } just Runs

        val result = authRepository.obtainAccessToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isEqualTo("new_access_token")
        coVerifySequence {
            tokenStorage.getAccessToken()
            timeSource.getCurrentTime()
            apiMapper.refreshAuthToken("refresh_token")
            apiMapper.getAuthToken(ACCESS_CODE, REDIRECT_URI)
            accessTokenResponseConverter.convert(accessTokenResponse)
            tokenStorage.storeAccessToken(newAccessTokenInfo)
        }
    }

    @Test
    fun `getAccessToken should throw NullAccessTokenException if no token available`() = runTest {
        every { tokenStorage.getAccessToken() } returns null

        assertThrows<NullAccessTokenException> {
            authRepository.getAccessToken()
        }
    }

    @Test
    fun `clear should clear token storage`() {
        every { tokenStorage.clear() } just Runs

        authRepository.clear()

        verify { tokenStorage.clear() }
    }

    private companion object {

        const val ACCESS_CODE = "sample_access_code"
        const val REDIRECT_URI = "sample_redirect_uri"
    }
}
