package com.example.spotify.data.converter

import com.example.spotify.data.models.network.ArtistResponse
import com.example.spotify.data.models.network.ImageResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [ArtistResponseToEntityConverter]
 */
class ArtistResponseToEntityConverterTest {

    private val converter = ArtistResponseToEntityConverter()

    @Test
    fun convertTest() {
        val artistResponse = ArtistResponse(
            id = "123",
            name = "Artist Name",
            popularity = 90,
            genres = listOf("Pop", "Jazz"),
            images = listOf(
                ImageResponse(url = "bigImageUrl"),
                ImageResponse(url = "smallImageUrl")
            )
        )

        val artistEntity = converter.convert(artistResponse)

        assertThat(artistEntity.id).isEqualTo("123")
        assertThat(artistEntity.name).isEqualTo("Artist Name")
        assertThat(artistEntity.popularity).isEqualTo(90)
        assertThat(artistEntity.genres).isEqualTo(listOf("Pop", "Jazz"))
        assertThat(artistEntity.smallImage).isEqualTo("smallImageUrl")
        assertThat(artistEntity.bigImage).isEqualTo("bigImageUrl")
    }

    @Test
    fun `convert if empty images list`() {
        val artistResponse = ArtistResponse(
            id = "123",
            name = "Artist Name",
            popularity = 90,
            genres = listOf("Pop", "Jazz"),
            images = emptyList()
        )

        val artistEntity = converter.convert(artistResponse)

        assertThat(artistEntity.smallImage).isEmpty()
        assertThat(artistEntity.bigImage).isEmpty()
    }

    @Test
    fun `convert if single image in list`() {
        val artistResponse = ArtistResponse(
            id = "123",
            name = "Artist Name",
            popularity = 90,
            genres = listOf("Pop", "Jazz"),
            images = listOf(
                ImageResponse(url = "onlyImageUrl")
            )
        )

        val artistEntity = converter.convert(artistResponse)

        assertThat(artistEntity.smallImage).isEqualTo("onlyImageUrl")
        assertThat(artistEntity.bigImage).isEqualTo("onlyImageUrl")
    }
}
