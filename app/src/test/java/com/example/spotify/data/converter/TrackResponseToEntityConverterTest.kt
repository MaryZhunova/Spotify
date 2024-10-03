package com.example.spotify.data.converter

import com.example.spotify.domain.models.AlbumInfo
import com.example.spotify.data.models.network.AlbumResponse
import com.example.spotify.data.models.network.ShortArtistResponse
import com.example.spotify.data.models.network.ImageResponse
import com.example.spotify.data.models.network.TrackResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [TrackResponseToEntityConverter]
 */
class TrackResponseToEntityConverterTest {

    private val converter = TrackResponseToEntityConverter()

    @Test
    fun convertTest() {
        val albumResponse = AlbumResponse(
            id = "album1",
            name = "Album Name",
            images = listOf(
                ImageResponse(url = "imageUrl1"),
                ImageResponse(url = "imageUrl2")
            ),
            releaseDate = "1981-12",
            releaseDatePrecision = "month",
        )
        val albumInfo = AlbumInfo(
            id = "album1",
            name = "Album Name",
            image = "imageUrl2"
        )
        val trackResponse = TrackResponse(
            id = "1",
            name = "Track Name",
            previewUrl = "https://example.com/preview",
            duration = 180,
            artists = listOf(
                ShortArtistResponse(id = "artist1", name = "Artist 1"),
                ShortArtistResponse(id = "artist2", name = "Artist 2")
            ),
            album = albumResponse,
            isExplicit = true,
            isPlayable = true,
            popularity = 85
        )

        val trackEntity = converter.convert(trackResponse)

        assertThat(trackEntity.id).isEqualTo("1")
        assertThat(trackEntity.name).isEqualTo("Track Name")
        assertThat(trackEntity.previewUrl).isEqualTo("https://example.com/preview")
        assertThat(trackEntity.duration).isEqualTo(180)
        assertThat(trackEntity.artistsId).containsExactly("artist1", "artist2")
        assertThat(trackEntity.artistsName).containsExactly("Artist 1", "Artist 2")
        assertThat(trackEntity.album).isEqualTo(albumInfo)
        assertThat(trackEntity.isExplicit).isTrue()
        assertThat(trackEntity.isPlayable).isTrue()
        assertThat(trackEntity.popularity).isEqualTo(85)
    }

    @Test
    fun `convertTest if empty album images`() {
        val albumResponse = AlbumResponse(
            id = "album1",
            name = "Album Name",
            images = emptyList(),
            releaseDate = "1981-12",
            releaseDatePrecision = "month",
        )
        val albumInfo = AlbumInfo(
            id = "album1",
            name = "Album Name",
            image = ""
        )
        val trackResponse = TrackResponse(
            id = "1",
            name = "Track Name",
            previewUrl = "https://example.com/preview",
            duration = 180,
            artists = listOf(
                ShortArtistResponse(id = "artist1", name = "Artist 1"),
                ShortArtistResponse(id = "artist2", name = "Artist 2")
            ),
            album = albumResponse,
            isExplicit = true,
            isPlayable = true,
            popularity = 85
        )

        val trackEntity = converter.convert(trackResponse)

        assertThat(trackEntity.id).isEqualTo("1")
        assertThat(trackEntity.name).isEqualTo("Track Name")
        assertThat(trackEntity.previewUrl).isEqualTo("https://example.com/preview")
        assertThat(trackEntity.duration).isEqualTo(180)
        assertThat(trackEntity.artistsId).containsExactly("artist1", "artist2")
        assertThat(trackEntity.artistsName).containsExactly("Artist 1", "Artist 2")
        assertThat(trackEntity.album).isEqualTo(albumInfo)
        assertThat(trackEntity.isExplicit).isTrue()
        assertThat(trackEntity.isPlayable).isTrue()
        assertThat(trackEntity.popularity).isEqualTo(85)
    }
}
