package com.example.spotify.data

import com.example.spotify.data.converter.ArtistEntityToInfoConverter
import com.example.spotify.data.converter.ArtistResponseToEntityConverter
import com.example.spotify.data.converter.TrackEntityToInfoConverter
import com.example.spotify.data.converter.TrackResponseToEntityConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.db.ArtistDao
import com.example.spotify.data.db.TrackDao
import com.example.spotify.data.models.db.ArtistEntity
import com.example.spotify.data.models.db.TrackEntity
import com.example.spotify.data.models.network.ArtistResponse
import com.example.spotify.data.models.network.TopArtistsResponse
import com.example.spotify.data.models.network.TopTracksResponse
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.data.models.network.UserProfileResponse
import com.example.spotify.data.network.mappers.SpotifyUserStatsApiMapper
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Тесты [SpotifyUserStatsRepositoryImpl]
 */
class SpotifyUserStatsRepositoryImplTest {

    private val apiMapper: SpotifyUserStatsApiMapper = mockk()
    private val userProfileConverter: UserProfileResponseToInfoConverter = mockk()
    private val trackResponseConverter: TrackResponseToEntityConverter = mockk()
    private val trackEntityConverter: TrackEntityToInfoConverter = mockk()
    private val artistResponseConverter: ArtistResponseToEntityConverter = mockk()
    private val artistEntityConverter: ArtistEntityToInfoConverter = mockk()
    private val authRepository: AuthRepository = mockk()
    private val artistDao: ArtistDao = mockk()
    private val trackDao: TrackDao = mockk()

    private val spotifyUserStatsRepository = SpotifyUserStatsRepositoryImpl(
        apiMapper,
        userProfileConverter,
        trackResponseConverter,
        trackEntityConverter,
        artistResponseConverter,
        artistEntityConverter,
        authRepository,
        artistDao,
        trackDao
    )

    @Test
    fun getCurrentUserProfileTest() = runTest {
        val accessToken = "access-token"
        val userProfileResponse = mockk<UserProfileResponse>()
        val userProfileInfo = mockk<UserProfileInfo>()

        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getCurrentUserProfile(accessToken) } returns userProfileResponse
        every { userProfileConverter.convert(userProfileResponse) } returns userProfileInfo

        val result = spotifyUserStatsRepository.getCurrentUserProfile()

        assertThat(result).isEqualTo(userProfileInfo)

        coVerifySequence {
            authRepository.getAccessToken()
            apiMapper.getCurrentUserProfile(accessToken)
            userProfileConverter.convert(userProfileResponse)
        }
    }

    @Test
    fun getTopTracksTest() = runTest {
        val timeRange = "short_term"
        val limit = 10
        val accessToken = "access-token"
        val trackResponse = mockk<TrackResponse>()
        val trackResponse2 = mockk<TrackResponse>()
        val initialResponse = mockk<TopTracksResponse> {
            every { items } returns listOf(trackResponse)
            every { next } returns "next-url"
        }
        val nextPageResponse = mockk<TopTracksResponse> {
            every { items } returns listOf(trackResponse2)
            every { next } returns null
        }
        val trackEntity = mockk<TrackEntity>()
        val trackEntity2 = mockk<TrackEntity>()
        val trackInfo = mockk<TrackInfo>()
        val trackInfo2 = mockk<TrackInfo>()

        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getTopTracks(accessToken, timeRange, limit) } returns initialResponse
        coEvery { apiMapper.getTopTracksNextPage(accessToken, "next-url") } returns nextPageResponse
        every { trackResponseConverter.convert(trackResponse) } returns trackEntity
        every { trackResponseConverter.convert(trackResponse2) } returns trackEntity2
        every { trackEntityConverter.convert(trackEntity) } returns trackInfo
        every { trackEntityConverter.convert(trackEntity2) } returns trackInfo2
        every { trackDao.insertAll(*anyVararg()) } just Runs

        val result = spotifyUserStatsRepository.getTopTracks(timeRange, limit)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(trackInfo)
        assertThat(result[1]).isEqualTo(trackInfo2)

        coVerifySequence {
            authRepository.getAccessToken()
            apiMapper.getTopTracks(accessToken, timeRange, limit)
            apiMapper.getTopTracksNextPage(accessToken, "next-url")
            trackResponseConverter.convert(trackResponse)
            trackResponseConverter.convert(trackResponse2)
            trackDao.insertAll(*listOf(trackEntity, trackEntity2).toTypedArray())
            trackEntityConverter.convert(trackEntity)
            trackEntityConverter.convert(trackEntity2)
        }
    }

    @Test
    fun getTopTracksByArtistIdTest() = runTest {
        val artistId = "artist-id"
        val trackEntity = mockk<TrackEntity>()
        val trackInfo = mockk<TrackInfo>()
        every { trackDao.findTracksByArtistId(artistId) } returns listOf(trackEntity)
        every { trackEntityConverter.convert(trackEntity) } returns trackInfo

        val result = spotifyUserStatsRepository.getTopTracksByArtistId(artistId)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(trackInfo)

        verifySequence {
            trackDao.findTracksByArtistId(artistId)
            trackEntityConverter.convert(trackEntity)
        }
    }

    @Test
    fun getTopArtistsTests() = runTest {
        val timeRange = "short_term"
        val limit = 10
        val accessToken = "access-token"
        val artistResponse = mockk<ArtistResponse>()
        val artistResponse2 = mockk<ArtistResponse>()
        val initialResponse = mockk<TopArtistsResponse> {
            every { items } returns listOf(artistResponse)
            every { next } returns "next-url"
        }
        val nextPageResponse = mockk<TopArtistsResponse> {
            every { items } returns listOf(artistResponse2)
            every { next } returns null
        }
        val artistEntity = mockk<ArtistEntity>()
        val artistEntity2 = mockk<ArtistEntity>()
        val artistInfo = mockk<ArtistInfo>()
        val artistInfo2 = mockk<ArtistInfo>()

        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getTopArtists(accessToken, timeRange, limit) } returns initialResponse
        coEvery { apiMapper.getTopArtistsNextPage(accessToken, "next-url") } returns nextPageResponse
        every { artistResponseConverter.convert(artistResponse) } returns artistEntity
        every { artistResponseConverter.convert(artistResponse2) } returns artistEntity2
        every { artistEntityConverter.convert(artistEntity) } returns artistInfo
        every { artistEntityConverter.convert(artistEntity2) } returns artistInfo2
        every { artistDao.insertAll(*anyVararg()) } just Runs

        val result = spotifyUserStatsRepository.getTopArtists(timeRange, limit)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(artistInfo)
        assertThat(result[1]).isEqualTo(artistInfo2)

        coVerifySequence {
            authRepository.getAccessToken()
            apiMapper.getTopArtists(accessToken, timeRange, limit)
            apiMapper.getTopArtistsNextPage(accessToken, "next-url")
            artistResponseConverter.convert(artistResponse)
            artistResponseConverter.convert(artistResponse2)
            artistDao.insertAll(*listOf(artistEntity, artistEntity2).toTypedArray())
            artistEntityConverter.convert(artistEntity)
            artistEntityConverter.convert(artistEntity2)
        }
    }
}