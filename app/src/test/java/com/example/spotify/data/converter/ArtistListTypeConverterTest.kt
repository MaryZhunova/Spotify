package com.example.spotify.data.converter

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [ArtistListTypeConverter]
 */
class ArtistListTypeConverterTest {

    private val converter = ArtistListTypeConverter()

    @Test
    fun fromArtistListTest() {
        val artistList = listOf("artist1", "artist2", "artist3")

        val result = converter.fromArtistList(artistList)

        assertThat(result).isEqualTo("artist1, artist2, artist3")
    }

    @Test
    fun `fromArtistListTest if empty list`() {
        val artistList = listOf<String>()

        val result = converter.fromArtistList(artistList)

        assertThat(result).isEmpty()
    }

    @Test
    fun toArtistListTest() {
        val artistString = "artist1, artist2, artist3"

        val result = converter.toArtistList(artistString)

        assertThat(result).containsExactly("artist1", "artist2", "artist3")
    }

    @Test
    fun `toArtistList if empty string`() {
        val artistString = ""

        val result = converter.toArtistList(artistString)

        assertThat(result).isEmpty()
    }

    @Test
    fun `toArtistList if blank string`() {
        val artistString = " "

        val result = converter.toArtistList(artistString)

        assertThat(result).isEmpty()
    }
}
