package com.example.spotify.data.storage

import com.example.spotify.domain.models.UserProfileInfo
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [UserProfileInfoSerializer]
 */
class UserProfileInfoSerializerTest {

    private val serializer = UserProfileInfoSerializer()

    @Test
    fun `serialize should convert UserProfileInfo to byte array`() {
        val userProfileInfo = UserProfileInfo(
            id = "user123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            image = "http://example.com/image.jpg",
            country = "US",
            product = "premium"
        )

        val result = serializer.serialize(userProfileInfo)

        val expected = "user123,John Doe,john.doe@example.com,http://example.com/image.jpg,US,premium".toByteArray(Charsets.UTF_8)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deserialize should convert byte array back to UserProfileInfo`() {
        val inputData = "user123,John Doe,john.doe@example.com,http://example.com/image.jpg,US,premium".toByteArray(Charsets.UTF_8)

        val result = serializer.deserialize(inputData)

        val expected = UserProfileInfo(
            id = "user123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            image = "http://example.com/image.jpg",
            country = "US",
            product = "premium"
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deserialize should handle null image`() {
        val inputData = "user123,John Doe,john.doe@example.com,,US,premium".toByteArray(Charsets.UTF_8)

        val result = serializer.deserialize(inputData)

        val expected = UserProfileInfo(
            id = "user123",
            displayName = "John Doe",
            email = "john.doe@example.com",
            image = null,
            country = "US",
            product = "premium"
        )

        assertThat(result).isEqualTo(expected)
    }
}