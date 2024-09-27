package com.example.spotify.data

import com.example.spotify.data.converter.AudioFeaturesResponseToInfoConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.models.network.ArtistsTopTracksResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse
import com.example.spotify.data.models.network.AudioFeaturesResponse
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapper
import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.TrackInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Тесты [SpotifyInfoRepositoryImpl]
 */
@ExperimentalCoroutinesApi
class SpotifyInfoRepositoryImplTest {

    private val apiMapper: SpotifyInfoApiMapper = mockk()
    private val trackInfoConverter: TrackResponseToInfoConverter = mockk()
    private val audioFeaturesConverter: AudioFeaturesResponseToInfoConverter = mockk()

    private val spotifyInfoRepository = SpotifyInfoRepositoryImpl(
        apiMapper,
        trackInfoConverter,
        audioFeaturesConverter
    )

    @Test
    fun getArtistsTopTracksTest() = runTest {
        val artistId = "artist-id"
        val accessToken = "access-token"
        val trackResponse = mockk<TrackResponse> {
            every { id } returns "1"
        }
        val apiResponse = mockk<ArtistsTopTracksResponse> {
            every { tracks } returns listOf(trackResponse)
        }
        val info = mockk<TrackInfo> {
            every {
                copy(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns this
        }

        val featuresResponse = mockk<AudioFeaturesResponse>()
        val featuresApiResponse = mockk<AudioFeaturesListResponse> {
            every { audioFeatures } returns listOf(featuresResponse)
        }
        val featuresInfo = mockk<AudioFeaturesInfo> {
            every { id } returns "1"
        }
        coEvery { apiMapper.getTracksAudioFeatures(accessToken, "1") } returns featuresApiResponse
        coEvery { apiMapper.getArtistsTopTracks(accessToken, artistId) } returns apiResponse
        every { trackInfoConverter.convert(any()) } returns info
        every { audioFeaturesConverter.convert(any()) } returns featuresInfo

        val result = spotifyInfoRepository.getArtistsTopTracks(accessToken, artistId)

        assertThat(result).isEqualTo(listOf(info))

        coVerifySequence {
            apiMapper.getArtistsTopTracks(accessToken, artistId)
            apiMapper.getTracksAudioFeatures(accessToken, "1")
            audioFeaturesConverter.convert(featuresResponse)
            trackInfoConverter.convert(trackResponse)
        }
    }

    @Test
    fun getTracksAudioFeaturesTest() = runTest {
        val accessToken = "access-token"
        val trackId = "1"
        val featuresResponse = mockk<AudioFeaturesResponse>()
        val featuresApiResponse = mockk<AudioFeaturesListResponse> {
            every { audioFeatures } returns listOf(featuresResponse)
        }
        val featuresInfo = mockk<AudioFeaturesInfo> {
            every { id } returns trackId
        }
        coEvery { apiMapper.getTracksAudioFeatures(accessToken, trackId) } returns featuresApiResponse
        every { audioFeaturesConverter.convert(any()) } returns featuresInfo

        val result = spotifyInfoRepository.getTracksAudioFeatures(accessToken, listOf(trackId))

        assertThat(result).isEqualTo(listOf(featuresInfo))

        coVerifySequence {
            apiMapper.getTracksAudioFeatures(accessToken, trackId)
            audioFeaturesConverter.convert(featuresResponse)
        }

    }
}
