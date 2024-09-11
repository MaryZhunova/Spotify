package com.example.spotify.data.converter

import com.example.spotify.models.data.UserProfileInfo
import com.example.spotify.models.data.net.Image
import com.example.spotify.models.data.net.UserProfileResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [ UserProfileResponseToInfoConverter]
 */
class UserProfileResponseToInfoConverterTest {

    private val converter = UserProfileResponseToInfoConverter()

    @Test
    fun convertTest() {

        val response = UserProfileResponse(
            id = "123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            images = listOf(
                Image(url = "http://example.com/image.jpg")
            ),
            country = "US",
            product = "premium"
        )

        val expected = UserProfileInfo(
            id = "123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            image = "http://example.com/image.jpg",
            country = "United States",
            product = "premium"
        )

        val result = converter.convert(response)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `convert if images list empty`() {

        val response = UserProfileResponse(
            id = "123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            images = emptyList(),
            country = "US",
            product = "premium"
        )

        val expected = UserProfileInfo(
            id = "123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            image = null,
            country = "United States",
            product = "premium"
        )

        val result = converter.convert(response)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `convert if country code invalid`() {

        val response = UserProfileResponse(
            id = "123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            images = listOf(Image(url = "http://example.com/image.jpg")),
            country = "ZZ",
            product = "premium"
        )

        val expected = UserProfileInfo(
            id = "123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            image = "http://example.com/image.jpg",
            country = "",
            product = "premium"
        )

        val result = converter.convert(response)

        assertThat(result).isEqualTo(expected)
    }
}