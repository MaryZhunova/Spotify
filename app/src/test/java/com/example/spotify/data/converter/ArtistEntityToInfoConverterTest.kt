package com.example.spotify.data.converter

import com.example.spotify.data.models.db.ArtistEntity
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [ArtistEntityToInfoConverter]
 */
class ArtistEntityToInfoConverterTest {

    private val converter = ArtistEntityToInfoConverter()

    @Test
    fun convertTest() {
        val artistEntity = ArtistEntity(
            id = "123",
            name = "Artist Name",
            popularity = 80,
            genres = listOf("Pop", "Rock"),
            bigImage = "bigImageUrl",
            smallImage = "smallImageUrl"
        )

        val artistInfo = converter.convert(artistEntity)

        assertThat(artistInfo.id).isEqualTo("123")
        assertThat(artistInfo.name).isEqualTo("Artist Name")
        assertThat(artistInfo.popularity).isEqualTo(80)
        assertThat(artistInfo.genres).isEqualTo("Pop, Rock")
        assertThat(artistInfo.image).isEqualTo("bigImageUrl")
    }

    @Test
    fun `convert if bigImage is blank`() {
        val artistEntity = ArtistEntity(
            id = "123",
            name = "Artist Name",
            popularity = 80,
            genres = listOf("Pop", "Rock"),
            bigImage = "",
            smallImage = "smallImageUrl"
        )

        val artistInfo = converter.convert(artistEntity)

        assertThat(artistInfo.image).isEqualTo("smallImageUrl")
    }
}