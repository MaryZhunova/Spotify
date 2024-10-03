package com.example.spotify.data.converter

import com.example.spotify.data.db.TrackDao
import com.example.spotify.data.models.network.AlbumResponse
import com.example.spotify.data.models.network.ShortArtistResponse
import com.example.spotify.data.models.network.ImageResponse
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.domain.models.AlbumInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * Тесты [TrackResponseToInfoConverter]
 */
class TrackResponseToInfoConverterTest {

    private val trackDao = mockk<TrackDao>()
    private val converter = TrackResponseToInfoConverter(trackDao)

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `convert should return correct TrackInfo`(isInDB: Boolean) {
        val albumResponse = AlbumResponse(
            id = "album_id",
            name = "Album Name",
            images = listOf(
                ImageResponse(url = "http://example.com/image1"),
                ImageResponse(url = "http://example.com/image2")
            ),
            releaseDate = "12.12.12",
            releaseDatePrecision = "day"
        )
        val shortArtistResponse = ShortArtistResponse(
            id = "artist_id",
            name = "Artist Name",
        )
        val response = TrackResponse(
            id = "track_id",
            name = "Track Name",
            previewUrl = "http://example.com/preview",
            duration = 180000,
            artists = listOf(shortArtistResponse),
            album = albumResponse,
            isExplicit = true,
            isPlayable = true,
            popularity = 85
        )

        every { trackDao.isTrackInDatabase("Track Name") } returns isInDB

        val result = converter.convert(response)

        assertThat(result.id).isEqualTo("track_id")
        assertThat(result.name).isEqualTo("Track Name")
        assertThat(result.previewUrl).isEqualTo("http://example.com/preview")
        assertThat(result.duration).isEqualTo(180000)
        assertThat(result.artists).isEqualTo("Artist Name")
        assertThat(result.album).isEqualTo(
            AlbumInfo(
                id = "album_id",
                name = "Album Name",
                image = "http://example.com/image2"
            )
        )
        assertThat(result.isExplicit).isTrue()
        assertThat(result.isPlayable).isTrue()
        assertThat(result.popularity).isEqualTo(85)
        assertThat(result.isFavorite).isEqualTo(isInDB)
    }
}
