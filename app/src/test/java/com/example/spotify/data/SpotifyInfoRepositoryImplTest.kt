package com.example.spotify.data

import com.example.spotify.data.converter.ArtistEntityToInfoConverter
import com.example.spotify.data.converter.TrackEntityToInfoConverter
import com.example.spotify.data.db.ArtistDao
import com.example.spotify.data.db.TrackDao
import com.example.spotify.data.models.db.ArtistEntity
import com.example.spotify.data.models.network.Album
import com.example.spotify.data.models.network.Artist
import com.example.spotify.data.models.network.ArtistsTopTracksResponse
import com.example.spotify.data.models.network.Image
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapper
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.AlbumInfo
import com.example.spotify.domain.models.ArtistInfo
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
    private val trackConverter: TrackEntityToInfoConverter = mockk()
    private val artistConverter: ArtistEntityToInfoConverter = mockk()
    private val artistDao: ArtistDao = mockk()
    private val trackDao: TrackDao = mockk()

    private val spotifyInfoRepository = SpotifyInfoRepositoryImpl(
        apiMapper,
        authRepository,
        trackConverter,
        artistConverter,
        artistDao,
        trackDao
    )

    @Test
    fun getArtistsTopTracksTest() = runTest {
        val artistId = "artist-id"
        val accessToken = "access-token"
        val trackName = "Track Name"
        val apiResponse = ArtistsTopTracksResponse(
            tracks = listOf(
                TrackResponse(
                    id = "track-id",
                    name = trackName,
                    previewUrl = "preview-url",
                    duration = 200,
                    artists = listOf(Artist(id = "artist-id", name = "Artist Name")),
                    album = Album(
                        id = "album-id",
                        name = "Album Name",
                        images = listOf(Image(url = "image-url")),
                        releaseDate = "2024-01-01",
                        releaseDatePrecision = "day"
                    ),
                    isExplicit = false,
                    isPlayable = true,
                    popularity = 100
                )
            )
        )
        coEvery { authRepository.getAccessToken() } returns accessToken
        coEvery { apiMapper.getArtistsTopTracks(accessToken, artistId) } returns apiResponse
        every { trackDao.isTrackInDatabase(any()) } returns true

        val result = spotifyInfoRepository.getArtistsTopTracks(artistId)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo("track-id")
        assertThat(result[0].name).isEqualTo(trackName)
        assertThat(result[0].previewUrl).isEqualTo("preview-url")
        assertThat(result[0].duration).isEqualTo(200)
        assertThat(result[0].artists).isEqualTo("Artist Name")
        assertThat(result[0].album).isEqualTo(
            AlbumInfo(
                id = "album-id",
                name = "Album Name",
                image = "image-url"
            )
        )
        assertThat(result[0].isExplicit).isFalse()
        assertThat(result[0].isPlayable).isTrue()
        assertThat(result[0].popularity).isEqualTo(100)
        assertThat(result[0].isFavorite).isTrue()

        coVerifySequence {
            authRepository.getAccessToken()
            apiMapper.getArtistsTopTracks(accessToken, artistId)
            trackDao.isTrackInDatabase(trackName)
        }
    }

    @Test
    fun getArtistsInfoTest() = runTest {
        val artistId = "artist-id"
        val artistEntity = ArtistEntity(
            id = artistId,
            name = "Artist Name",
            popularity = 100,
            genres = "Genre",
            smallImage = "image-url",
            bigImage = ""
        )
        val artistInfo = ArtistInfo(
            id = artistId,
            name = "Artist Name",
            popularity = 100,
            genres = "Genre",
            image = "image-url"
        )
        every { artistDao.getById(artistId) } returns artistEntity
        every { artistConverter.convert(artistEntity) } returns artistInfo

        val result = spotifyInfoRepository.getArtistsInfo(artistId)

        assertThat(result.id).isEqualTo(artistId)
        assertThat(result.name).isEqualTo("Artist Name")
        assertThat(result.popularity).isEqualTo(100)
        assertThat(result.genres).isEqualTo("Genre")
        assertThat(result.image).isEqualTo("image-url")

        verifySequence {
            artistDao.getById(artistId)
            artistConverter.convert(artistEntity)
        }
    }
}
