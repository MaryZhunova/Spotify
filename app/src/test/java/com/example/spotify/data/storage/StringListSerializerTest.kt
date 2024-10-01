package com.example.spotify.data.storage

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [StringListSerializer]
 */
class StringListSerializerTest {

    private val serializer = StringListSerializer()

    @Test
    fun `serialize should convert list of strings to byte array`() {
        val inputList = listOf("apple", "banana", "cherry")

        val result = serializer.serialize(inputList)

        val expected = "apple,banana,cherry".toByteArray(Charsets.UTF_8)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deserialize should convert byte array back to list of strings`() {
        val inputData = "apple,banana,cherry".toByteArray(Charsets.UTF_8)

        val result = serializer.deserialize(inputData)

        val expected = listOf("apple", "banana", "cherry")

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deserialize should handle byte array with extra commas`() {
        val inputData = "apple,,banana,cherry,".toByteArray(Charsets.UTF_8)

        val result = serializer.deserialize(inputData)

        val expected = listOf("apple", "", "banana", "cherry", "")

        assertThat(result).isEqualTo(expected)
    }
}