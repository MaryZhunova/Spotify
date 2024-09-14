package com.example.spotify.data.auth.converter

import com.example.spotify.models.data.auth.net.AccessTokenResponse
import com.example.spotify.utils.TimeSource
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

/**
 * Тесты для [AccessTokenResponseToInfoConverter]
 */
class AccessTokenResponseToInfoConverterTest {

    private val timeSource: TimeSource = mockk()

    private val converter = AccessTokenResponseToInfoConverter(timeSource)

    @Test
    fun convertTest() {
        val accessTokenResponse = AccessTokenResponse(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "user-read-private user-read-email"
        )
        every { timeSource.getCurrentTime() } returns 3800 * 1000

        val result = converter.convert(accessTokenResponse)

        assertThat(result.accessToken).isEqualTo(accessTokenResponse.accessToken)
        assertThat(result.refreshToken).isEqualTo(accessTokenResponse.refreshToken)
        assertThat(result.tokenType).isEqualTo(accessTokenResponse.tokenType)
        assertThat(result.expiresAt).isEqualTo(3600 * 1000 + 3800 * 1000)
    }
}