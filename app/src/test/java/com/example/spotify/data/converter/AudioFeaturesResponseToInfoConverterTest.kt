package com.example.spotify.data.converter

import com.example.spotify.data.models.network.AudioFeaturesResponse
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat

/**
 * Тесты [AudioFeaturesResponseToInfoConverter]
 */
class AudioFeaturesResponseToInfoConverterTest {

    private val converter = AudioFeaturesResponseToInfoConverter()

    @Test
    fun `convert should return correct AudioFeaturesInfo`() {
        val response = AudioFeaturesResponse(
            acousticness = 0.5,
            analysisUrl = "http://example.com/analysis",
            danceability = 0.7,
            durationMs = 200000,
            energy = 0.8,
            id = "track_id",
            instrumentalness = 0.0,
            key = 1,
            liveness = 0.3,
            loudness = -5.0,
            mode = 1,
            speechiness = 0.05,
            tempo = 120.0,
            timeSignature = 4,
            trackHref = "http://example.com/track",
            type = "track",
            uri = "spotify:track:track_id",
            valence = 0.6
        )

        val result = converter.convert(response)

        assertThat(result.acousticness).isEqualTo(0.5)
        assertThat(result.analysis_url).isEqualTo("http://example.com/analysis")
        assertThat(result.danceability).isEqualTo(0.7)
        assertThat(result.duration_ms).isEqualTo(200000)
        assertThat(result.energy).isEqualTo(0.8)
        assertThat(result.id).isEqualTo("track_id")
        assertThat(result.instrumentalness).isEqualTo(0.0)
        assertThat(result.key).isEqualTo(1)
        assertThat(result.liveness).isEqualTo(0.3)
        assertThat(result.loudness).isEqualTo(((-5.0 + 60) / 60))
        assertThat(result.mode).isEqualTo(1)
        assertThat(result.speechiness).isEqualTo(0.05)
        assertThat(result.tempo).isEqualTo(120.0)
        assertThat(result.time_signature).isEqualTo(4)
        assertThat(result.track_href).isEqualTo("http://example.com/track")
        assertThat(result.type).isEqualTo("track")
        assertThat(result.uri).isEqualTo("spotify:track:track_id")
        assertThat(result.valence).isEqualTo(0.6)
    }
}
