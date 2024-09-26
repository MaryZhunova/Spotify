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
import com.example.spotify.data.storage.SpotifyUserInfoStorage
import com.example.spotify.data.storage.SpotifyUserStatsStorage
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
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
    private val userStatsStorage: SpotifyUserStatsStorage = mockk()
    private val userInfoStorage: SpotifyUserInfoStorage = mockk()

    private val spotifyUserStatsRepository = SpotifyUserStatsRepositoryImpl(
        apiMapper,
        userProfileConverter,
        trackResponseConverter,
        trackEntityConverter,
        artistResponseConverter,
        artistEntityConverter,
        authRepository,
        artistDao,
        trackDao,
        userStatsStorage,
        userInfoStorage
    )

    @Test
    fun `getCurrentUserProfile from cache`() = runTest {
        val userProfileInfo = mockk<UserProfileInfo>()

        every { userInfoStorage.getCurrentUserInfo() } returns userProfileInfo

        val result = spotifyUserStatsRepository.getCurrentUserProfile()

        assertThat(result).isEqualTo(userProfileInfo)

        verifySequence {
            userInfoStorage.getCurrentUserInfo()
        }
        coVerify(inverse = true) {
            authRepository.getAccessToken()
            apiMapper.getCurrentUserProfile(any())
            userProfileConverter.convert(any())
        }
    }

    @Test
    fun getCurrentUserProfileTest() = runTest {
        val accessToken = "access-token"
        val userProfileResponse = mockk<UserProfileResponse>()
        val userProfileInfo = mockk<UserProfileInfo>()

        every { userInfoStorage.getCurrentUserInfo() } returns null
        every { userInfoStorage.setCurrentUserInfo(any()) } just Runs
        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getCurrentUserProfile(accessToken) } returns userProfileResponse
        every { userProfileConverter.convert(userProfileResponse) } returns userProfileInfo

        val result = spotifyUserStatsRepository.getCurrentUserProfile()

        assertThat(result).isEqualTo(userProfileInfo)

        coVerifySequence {
            userInfoStorage.getCurrentUserInfo()
            authRepository.getAccessToken()
            apiMapper.getCurrentUserProfile(accessToken)
            userProfileConverter.convert(userProfileResponse)
            userInfoStorage.setCurrentUserInfo(userProfileInfo)
        }
    }

    @Test
    fun `getTopTracks from cache`() = runTest {
        val idsList = listOf("1", "2")
        val timeRange = "short_term"
        val trackEntity = mockk<TrackEntity> {
            every { id } returns "1"
        }
        val trackEntity2 = mockk<TrackEntity> {
            every { id } returns "2"
        }
        val trackInfo = mockk<TrackInfo>()
        val trackInfo2 = mockk<TrackInfo>()

        every { userStatsStorage.getIdsList("${timeRange}_tracks") } returns idsList
        every { trackDao.getByIds(*idsList.toTypedArray()) } returns listOf(
            trackEntity,
            trackEntity2
        )
        every { trackEntityConverter.convert(trackEntity) } returns trackInfo
        every { trackEntityConverter.convert(trackEntity2) } returns trackInfo2

        val result = spotifyUserStatsRepository.getTopTracks(timeRange)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(trackInfo)
        assertThat(result[1]).isEqualTo(trackInfo2)

        coVerifySequence {
            userStatsStorage.getIdsList("${timeRange}_tracks")
            trackDao.getByIds(*idsList.toTypedArray())
            trackEntityConverter.convert(trackEntity)
            trackEntityConverter.convert(trackEntity2)
        }
        coVerify(inverse = true) {
            authRepository.getAccessToken()
            apiMapper.getTopTracks(any(), any(), any())
            apiMapper.getTopTracksNextPage(any(), any())
            trackResponseConverter.convert(any())
            trackDao.insertAll(any())
        }
    }

    @Test
    fun getTopTracksTest() = runTest {
        val timeRange = "short_term"
        val limit = 50
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
        val trackEntity = mockk<TrackEntity> {
            every { id } returns "1"
        }
        val trackEntity2 = mockk<TrackEntity> {
            every { id } returns "2"
        }
        val trackInfo = mockk<TrackInfo>()
        val trackInfo2 = mockk<TrackInfo>()

        every { userStatsStorage.getIdsList("${timeRange}_tracks") } returns null
        coEvery { userStatsStorage.setIdsList("${timeRange}_tracks", any()) } just Runs
        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getTopTracks(accessToken, timeRange, limit) } returns initialResponse
        coEvery { apiMapper.getTopTracksNextPage(accessToken, "next-url") } returns nextPageResponse
        every { trackResponseConverter.convert(trackResponse) } returns trackEntity
        every { trackResponseConverter.convert(trackResponse2) } returns trackEntity2
        every { trackEntityConverter.convert(trackEntity) } returns trackInfo
        every { trackEntityConverter.convert(trackEntity2) } returns trackInfo2
        every { trackDao.insertAll(*anyVararg()) } just Runs

        val result = spotifyUserStatsRepository.getTopTracks(timeRange)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(trackInfo)
        assertThat(result[1]).isEqualTo(trackInfo2)

        coVerifySequence {
            userStatsStorage.getIdsList("${timeRange}_tracks")
            authRepository.getAccessToken()
            apiMapper.getTopTracks(accessToken, timeRange, limit)
            apiMapper.getTopTracksNextPage(accessToken, "next-url")
            trackResponseConverter.convert(trackResponse)
            trackResponseConverter.convert(trackResponse2)
            trackDao.insertAll(*listOf(trackEntity, trackEntity2).toTypedArray())
            userStatsStorage.setIdsList("${timeRange}_tracks", listOf("1", "2"))
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
    fun `getTopArtists from cache`() = runTest {
        val idsList = listOf("1", "2")
        val timeRange = "short_term"
        val artistEntity = mockk<ArtistEntity> {
            every { id } returns "1"
        }
        val artistEntity2 = mockk<ArtistEntity> {
            every { id } returns "2"
        }
        val artistInfo = mockk<ArtistInfo>()
        val artistInfo2 = mockk<ArtistInfo>()

        coEvery { userStatsStorage.getIdsList("${timeRange}_artists") } returns idsList
        coEvery { artistDao.getByIds(*idsList.toTypedArray()) } returns listOf(
            artistEntity,
            artistEntity2
        )
        every { artistEntityConverter.convert(artistEntity) } returns artistInfo
        every { artistEntityConverter.convert(artistEntity2) } returns artistInfo2

        val result = spotifyUserStatsRepository.getTopArtists(timeRange)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(artistInfo)
        assertThat(result[1]).isEqualTo(artistInfo2)

        coVerifySequence {
            userStatsStorage.getIdsList("${timeRange}_artists")
            artistDao.getByIds(*idsList.toTypedArray())
            artistEntityConverter.convert(artistEntity)
            artistEntityConverter.convert(artistEntity2)
        }

        coVerify(inverse = true) {
            authRepository.getAccessToken()
            apiMapper.getTopArtists(any(), any(), any())
            apiMapper.getTopArtistsNextPage(any(), any())
            artistResponseConverter.convert(any())
            artistDao.insertAll(any())
        }
    }

    @Test
    fun getTopArtistsTests() = runTest {
        val timeRange = "short_term"
        val limit = 50
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
        val artistEntity = mockk<ArtistEntity> {
            every { id } returns "1"
        }
        val artistEntity2 = mockk<ArtistEntity> {
            every { id } returns "2"
        }
        val artistInfo = mockk<ArtistInfo>()
        val artistInfo2 = mockk<ArtistInfo>()

        coEvery { userStatsStorage.getIdsList("${timeRange}_artists") } returns null
        coEvery { userStatsStorage.setIdsList("${timeRange}_artists", any()) } just Runs
        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getTopArtists(accessToken, timeRange, limit) } returns initialResponse
        coEvery {
            apiMapper.getTopArtistsNextPage(
                accessToken,
                "next-url"
            )
        } returns nextPageResponse
        every { artistResponseConverter.convert(artistResponse) } returns artistEntity
        every { artistResponseConverter.convert(artistResponse2) } returns artistEntity2
        every { artistEntityConverter.convert(artistEntity) } returns artistInfo
        every { artistEntityConverter.convert(artistEntity2) } returns artistInfo2
        every { artistDao.insertAll(*anyVararg()) } just Runs

        val result = spotifyUserStatsRepository.getTopArtists(timeRange)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(artistInfo)
        assertThat(result[1]).isEqualTo(artistInfo2)

        coVerifySequence {
            userStatsStorage.getIdsList("${timeRange}_artists")
            authRepository.getAccessToken()
            apiMapper.getTopArtists(accessToken, timeRange, limit)
            apiMapper.getTopArtistsNextPage(accessToken, "next-url")
            artistResponseConverter.convert(artistResponse)
            artistResponseConverter.convert(artistResponse2)
            artistDao.insertAll(*listOf(artistEntity, artistEntity2).toTypedArray())
            userStatsStorage.setIdsList("${timeRange}_artists", listOf("1", "2"))
            artistEntityConverter.convert(artistEntity)
            artistEntityConverter.convert(artistEntity2)
        }
    }

    @Test
    fun getTopGenresTest() = runTest {
        val idsList = listOf("1", "2", "3", "4")
        val timeRange = "short_term"
        val artistEntity = mockk<ArtistEntity> {
            every { id } returns "1"
            every { genres } returns listOf("Rock")
            every { name } returns "Taylor"
        }
        val artistEntity2 = mockk<ArtistEntity> {
            every { id } returns "2"
            every { genres } returns listOf("Rock", "Pop")
            every { name } returns "Finneas"
        }
        val artistEntity3 = mockk<ArtistEntity> {
            every { id } returns "3"
            every { genres } returns listOf("Rock", "Pop", "Jazz")
            every { name } returns "Lorde"
        }
        val artistEntity4 = mockk<ArtistEntity> {
            every { id } returns "4"
            every { genres } returns listOf("Rock", "Pop", "Jazz", "Rap")
            every { name } returns "Kate"
        }

        coEvery { userStatsStorage.getIdsList("${timeRange}_artists") } returns idsList
        coEvery { artistDao.getByIds(*idsList.toTypedArray()) } returns listOf(
            artistEntity,
            artistEntity2,
            artistEntity3,
            artistEntity4
        )

        val result = spotifyUserStatsRepository.getTopGenres(timeRange)

        assertThat(result).hasSize(2)
        result[0].apply {
            assertThat(genre).isEqualTo("Rock")
            assertThat(numberOfArtists).isEqualTo(4)
            assertThat(artistNames).isEqualTo(listOf("Taylor", "Finneas", "Lorde", "Kate"))
        }
        result[1].apply {
            assertThat(genre).isEqualTo("Pop")
            assertThat(numberOfArtists).isEqualTo(3)
            assertThat(artistNames).isEqualTo(listOf("Finneas", "Lorde", "Kate"))
        }
    }

    @Test
    fun `getTopGenresTest empty`() = runTest {
        val idsList = listOf("1", "2")
        val timeRange = "short_term"
        val artistEntity = mockk<ArtistEntity> {
            every { id } returns "1"
            every { genres } returns listOf("Rock")
            every { name } returns "Taylor"
        }
        val artistEntity2 = mockk<ArtistEntity> {
            every { id } returns "2"
            every { genres } returns listOf("Rock", "Pop")
            every { name } returns "Finneas"
        }

        coEvery { userStatsStorage.getIdsList("${timeRange}_artists") } returns idsList
        coEvery { artistDao.getByIds(*idsList.toTypedArray()) } returns listOf(
            artistEntity,
            artistEntity2
        )

        val result = spotifyUserStatsRepository.getTopGenres(timeRange)

        assertThat(result).isEmpty()
    }
}