package com.example.spotify.data.auth.network

import com.example.spotify.data.auth.models.AccessTokenResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.Credentials
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import retrofit2.Response
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Call

/**
 * Тесты для [SpotifyAuthApiMapperImpl]
 */
@ExperimentalCoroutinesApi
class SpotifyAuthApiMapperImplTest {

    private val apiService = mockk<SpotifyAuthApiService>()
    private val mapper = SpotifyAuthApiMapperImpl(apiService)

    @BeforeEach
    fun setUp() {
        mockkStatic(Credentials::basic)
        every { Credentials.basic(any(), any()) } returns AUTH_CODE
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Credentials::basic)
    }

    @Test
    fun `test getAuthToken with successful response`() = runTest {
        val mockResponse = AccessTokenResponse(
            accessToken = "access_token",
            tokenType = "Bearer",
            refreshToken = "refresh_token",
            expiresIn = 3600,
            scope = "streaming"
        )
        val mockCall = mockk<Call<AccessTokenResponse>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.exchangeCodeForToken(any(), any(), any(), any()) } returns mockCall

        val result = mapper.getAuthToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isEqualTo(mockResponse)

        coVerify {
            apiService.exchangeCodeForToken(
                authorization = AUTH_CODE,
                code = ACCESS_CODE,
                redirectUri = REDIRECT_URI
            )
        }
    }

    @Test
    fun `test getAuthToken with error response`() = runTest {
        val response = Response.error<AccessTokenResponse>(400, "".toResponseBody())
        val mockCall = mockk<Call<AccessTokenResponse>>()
        every { mockCall.execute() } returns response
        coEvery { apiService.exchangeCodeForToken(any(), any(), any(), any()) } returns mockCall

        val result = mapper.getAuthToken(ACCESS_CODE, REDIRECT_URI)

        assertThat(result).isNull()

        coVerify {
            apiService.exchangeCodeForToken(
                authorization = AUTH_CODE,
                code = ACCESS_CODE,
                redirectUri = REDIRECT_URI
            )
        }
    }

    @Test
    fun `test refreshAuthToken with successful response`() = runTest {
        val mockResponse = AccessTokenResponse(
            accessToken = "access_token",
            tokenType = "Bearer",
            refreshToken = "new_refresh_token",
            expiresIn = 3600,
            scope = "streaming"
        )
        val mockCall = mockk<Call<AccessTokenResponse>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.refreshAuthToken(any(), any(), any()) } returns mockCall

        val result = mapper.refreshAuthToken(REFRESH_TOKEN)

        assertThat(result).isEqualTo(mockResponse)

        coVerify {
            apiService.refreshAuthToken(
                authorization = AUTH_CODE,
                token = REFRESH_TOKEN
            )
        }
    }

    @Test
    fun `test refreshAuthToken with error response`() = runTest {
        val response = Response.error<AccessTokenResponse>(400, "".toResponseBody())
        val mockCall = mockk<Call<AccessTokenResponse>>()
        every { mockCall.execute() } returns response
        coEvery { apiService.refreshAuthToken(any(), any(), any()) } returns mockCall

        val result = mapper.refreshAuthToken(REFRESH_TOKEN)

        assertThat(result).isNull()
        coVerify {
            apiService.refreshAuthToken(
                authorization = AUTH_CODE,
                token = REFRESH_TOKEN
            )
        }
    }

    private companion object {

        const val AUTH_CODE = "code"
        const val ACCESS_CODE = "sample_access_code"
        const val REDIRECT_URI = "sample_redirect_uri"
        const val REFRESH_TOKEN = "sample_refresh_token"

    }
}
