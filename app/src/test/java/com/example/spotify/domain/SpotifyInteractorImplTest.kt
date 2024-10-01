package com.example.spotify.domain

import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.AudioFeaturesInfo
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Тесты [SpotifyInteractorImpl]
 */
class SpotifyInteractorImplTest {

    private lateinit var statsRepository: SpotifyUserStatsRepository
    private lateinit var infoRepository: SpotifyInfoRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var interactor: SpotifyInteractorImpl

    private val accessToken = "mock_access_token"

    @BeforeEach
    fun setUp() {
        statsRepository = mockk()
        infoRepository = mockk()
        authRepository = mockk {
            every { getAccessToken() } returns accessToken
        }
        interactor = SpotifyInteractorImpl(statsRepository, infoRepository, authRepository)
    }

    @Test
    fun getArtistsTopTracksTest() = runTest {
        val artistId = "artist_id"
        val expectedTracks = listOf(mockk<TrackInfo>())

        coEvery { infoRepository.getArtistsTopTracks(accessToken, artistId) } returns expectedTracks

        val result = interactor.getArtistsTopTracks(artistId)

        assertThat(result).isEqualTo(expectedTracks)

        coVerify(exactly = 1) { infoRepository.getArtistsTopTracks(accessToken, artistId) }
    }

    @Test
    fun getArtistsInfoTest() = runTest {
        val artistId = "artist_id"
        val expectedInfo = mockk<ArtistInfo>()

        coEvery { statsRepository.getArtistsInfo(artistId) } returns expectedInfo

        val result = interactor.getArtistsInfo(artistId)

        assertThat(result).isEqualTo(expectedInfo)

        coVerify(exactly = 1) { statsRepository.getArtistsInfo(artistId) }
    }

    @Test
    fun getCurrentUserProfileTest() = runTest {
        val expectedProfile = mockk<UserProfileInfo>()

        coEvery { statsRepository.getCurrentUserProfile(accessToken) } returns expectedProfile

        val result = interactor.getCurrentUserProfile()

        assertThat(result).isEqualTo(expectedProfile)

        coVerify(exactly = 1) { statsRepository.getCurrentUserProfile(accessToken) }
    }

    @Test
    fun getTopTracksTest() = runTest {
        val timeRange = "short_term"
        val expectedTracks = listOf(mockk<TrackInfo> {
            every { id } returns "id"
            every { copy(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns this
        })
        val audioFeatures = listOf(mockk<AudioFeaturesInfo> {
            every { id } returns "id"
        })

        coEvery { statsRepository.getTopTracks(accessToken, timeRange) } returns expectedTracks
        coEvery {
            infoRepository.getTracksAudioFeatures(accessToken, listOf("id"))
        } returns audioFeatures

        val result = interactor.getTopTracks(timeRange)

        assertThat(result).hasSize(expectedTracks.size)

        coVerifySequence {
            statsRepository.getTopTracks(accessToken, timeRange)
            infoRepository.getTracksAudioFeatures(accessToken, listOf("id"))
        }
    }

    @Test
    fun getTopTracksByArtistIdTest() = runTest {
        val trackId = "id"
        val expectedTracks = listOf(mockk<TrackInfo> {
            every { id } returns trackId
            every { copy(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns this
        })
        val audioFeatures = listOf(mockk<AudioFeaturesInfo> {
            every { id } returns "id"
        })

        coEvery { statsRepository.getTopTracksByArtistId(trackId) } returns expectedTracks
        coEvery {
            infoRepository.getTracksAudioFeatures(
                accessToken,
                listOf(trackId)
            )
        } returns audioFeatures

        val result = interactor.getTopTracksByArtistId(trackId)

        assertThat(result).hasSize(expectedTracks.size)

        coVerifySequence {
            statsRepository.getTopTracksByArtistId(trackId)
            infoRepository.getTracksAudioFeatures(accessToken, listOf(trackId))
        }
    }

    @Test
    fun getTopArtistsTest() = runTest {
        val timeRange = "short_term"
        val expectedArtists = listOf(mockk<ArtistInfo>())

        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { statsRepository.getTopArtists(accessToken, timeRange) } returns expectedArtists

        val result = interactor.getTopArtists(timeRange)

        assertThat(result).isEqualTo(expectedArtists)

        coVerifySequence {
            authRepository.getAccessToken()
            statsRepository.getTopArtists(accessToken, timeRange)
        }
    }

    @Test
    fun getTopGenresTest() = runTest {
        val timeRange = "short_term"
        val expectedGenres = listOf(mockk<GenreInfo>())

        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { statsRepository.getTopGenres(accessToken, timeRange) } returns expectedGenres

        val result = interactor.getTopGenres(timeRange)

        assertThat(result).isEqualTo(expectedGenres)

        coVerifySequence {
            authRepository.getAccessToken()
            statsRepository.getTopGenres(accessToken, timeRange)
        }
    }

    @Test
    fun `clear should invoke clear on stats repository`() = runTest {
        coEvery { statsRepository.clear() } just Runs

        interactor.clear()

        coVerify { statsRepository.clear() }
    }
}