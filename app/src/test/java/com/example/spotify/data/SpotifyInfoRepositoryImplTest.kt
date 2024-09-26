package com.example.spotify.data

import com.example.spotify.data.converter.ArtistEntityToInfoConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.db.ArtistDao
import com.example.spotify.data.models.db.ArtistEntity
import com.example.spotify.data.models.network.ArtistsTopTracksResponse
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapper
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Тесты [SpotifyInfoRepositoryImpl]
 */
@ExperimentalCoroutinesApi
class SpotifyInfoRepositoryImplTest {

    private val apiMapper: SpotifyInfoApiMapper = mockk()
    private val authRepository: AuthRepository = mockk()
    private val trackInfoConverter: TrackResponseToInfoConverter = mockk()
    private val artistConverter: ArtistEntityToInfoConverter = mockk()
    private val artistDao: ArtistDao = mockk()

    private val spotifyInfoRepository = SpotifyInfoRepositoryImpl(
        apiMapper,
        authRepository,
        trackInfoConverter,
        artistConverter,
        artistDao
    )

    @Test
    fun getArtistsTopTracksTest() = runTest {
        val artistId = "artist-id"
        val accessToken = "access-token"
        val trackResponse = mockk<TrackResponse>()
        val apiResponse = mockk<ArtistsTopTracksResponse> {
            every { tracks } returns listOf(trackResponse)
        }
        val info = mockk<TrackInfo>()
        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getArtistsTopTracks(accessToken, artistId) } returns apiResponse
        every { trackInfoConverter.convert(any()) } returns info

        val result = spotifyInfoRepository.getArtistsTopTracks(artistId)

        assertThat(result).isEqualTo(listOf(info))

        coVerifySequence {
            authRepository.getAccessToken()
            apiMapper.getArtistsTopTracks(accessToken, artistId)
            trackInfoConverter.convert(trackResponse)
        }
    }

    @Test
    fun getArtistsInfoTest() = runTest {
        val artistId = "artist-id"
        val artistEntity = ArtistEntity(
            id = artistId,
            name = "Artist Name",
            popularity = 100,
            genres = listOf("Pop", "Jazz"),
            smallImage = "image-url",
            bigImage = ""
        )
        val artistInfo = ArtistInfo(
            id = artistId,
            name = "Artist Name",
            popularity = 100,
            genres = "Pop, Jazz",
            image = "image-url"
        )
        every { artistDao.getById(artistId) } returns artistEntity
        every { artistConverter.convert(artistEntity) } returns artistInfo

        val result = spotifyInfoRepository.getArtistsInfo(artistId)

        assertThat(result.id).isEqualTo(artistId)
        assertThat(result.name).isEqualTo("Artist Name")
        assertThat(result.popularity).isEqualTo(100)
        assertThat(result.genres).isEqualTo("Pop, Jazz")
        assertThat(result.image).isEqualTo("image-url")

        verifySequence {
            artistDao.getById(artistId)
            artistConverter.convert(artistEntity)
        }
    }
}
