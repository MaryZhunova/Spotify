package com.example.spotify.data.converter

import com.example.spotify.data.db.TrackEntity
import com.example.spotify.models.data.AlbumInfo
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тесты [TrackEntityToInfoConverter]
 */
class TrackEntityToInfoConverterTest {

    private val converter = TrackEntityToInfoConverter()

    @Test
    fun `convert should map TrackEntity to TrackInfo correctly`() {
        val album = AlbumInfo(id = "Album Id", name = "Album Name", image = "Album Image")
        val trackEntity = TrackEntity(
            id = "1",
            name = "Track Name",
            previewUrl = "https://preview.com",
            duration = 180,
            artistsId = listOf("Artist1Id", "Artist2Id"),
            artistsName = listOf("Artist1", "Artist2"),
            album = album,
            isExplicit = true,
            isPlayable = true,
            popularity = 75
        )

        val trackInfo = converter.convert(trackEntity)

        assertThat(trackInfo.id).isEqualTo("1")
        assertThat(trackInfo.name).isEqualTo("Track Name")
        assertThat(trackInfo.previewUrl).isEqualTo("https://preview.com")
        assertThat(trackInfo.duration).isEqualTo(180)
        assertThat(trackInfo.artists).isEqualTo("Artist1, Artist2")
        assertThat(trackInfo.album).isEqualTo(album)
        assertThat(trackInfo.isExplicit).isTrue()
        assertThat(trackInfo.isPlayable).isTrue()
        assertThat(trackInfo.popularity).isEqualTo(75)
    }
}
