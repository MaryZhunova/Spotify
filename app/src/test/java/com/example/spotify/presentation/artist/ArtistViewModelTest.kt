package com.example.spotify.presentation.artist

import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TopTrackInfo
import com.example.spotify.domain.models.TrackInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

/**
 * Тесты [ArtistViewModel]
 */
@ExperimentalCoroutinesApi
class ArtistViewModelTest {

    private val infoRepository: SpotifyInfoRepository = mockk()
    private val userRepository: SpotifyUserStatsRepository = mockk()

    private lateinit var viewModel: ArtistViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ArtistViewModel(infoRepository, userRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ParameterizedTest(name = "{index} => topTracks={0}, favoriteTracks={1}")
    @MethodSource("provideTestData")
    fun fetchTracksAndArtistTest(
        topTracks: List<TopTrackInfo>,
        favoriteTracks: List<TrackInfo>
    ) = runTest {
        val artistId = "artist-id"
        val artistInfo = mockk<ArtistInfo>()
        coEvery { infoRepository.getArtistsTopTracks(artistId) } returns topTracks
        coEvery { infoRepository.getArtistsInfo(artistId) } returns artistInfo
        coEvery { userRepository.getTopTracksByArtistId(artistId) } returns favoriteTracks

        viewModel.fetchTracksAndArtist(artistId).join()

        assertThat(viewModel.topTracks.value).isEqualTo(topTracks)
        assertThat(viewModel.artist.value).isEqualTo(artistInfo)
        assertThat(viewModel.favoriteTracks.value).isEqualTo(favoriteTracks)
        assertThat(viewModel.isLoading.value).isFalse()

        coVerifySequence {
            infoRepository.getArtistsTopTracks(artistId)
            infoRepository.getArtistsInfo(artistId)
            userRepository.getTopTracksByArtistId(artistId)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun changeIsHighlightedStateTest(
        isFav: Boolean
    ) = runTest {
        val artistId = "artist-id"
        val artistInfo = mockk<ArtistInfo>()
        val topTracks = listOf(mockk<TopTrackInfo> {
            every { isFavorite } returns isFav
        })
        val favoriteTracks = listOf(mockk<TrackInfo>())
        coEvery { infoRepository.getArtistsTopTracks(artistId) } returns topTracks
        coEvery { infoRepository.getArtistsInfo(artistId) } returns artistInfo
        coEvery { userRepository.getTopTracksByArtistId(artistId) } returns favoriteTracks

        viewModel.fetchTracksAndArtist(artistId).join()

        assertThat(viewModel.changeIsHighlightedState()).isEqualTo(isFav)
        assertThat(viewModel.isFavoriteHighlighted.value).isEqualTo(isFav)

        assertThat(viewModel.changeIsHighlightedState()).isEqualTo(isFav)
        assertThat(viewModel.isFavoriteHighlighted.value).isFalse()


    }

    companion object {
        @JvmStatic
        fun provideTestData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                // Case where both lists are empty
                listOf<TopTrackInfo>(), listOf<TrackInfo>()
            ),
            Arguments.of(
                // Case where topTracks is non-empty, favoriteTracks is empty
                listOf(mockk<TopTrackInfo>()), listOf<TrackInfo>()
            ),
            Arguments.of(
                // Case where topTracks is empty, favoriteTracks is non-empty
                listOf<TopTrackInfo>(), listOf(mockk<TrackInfo>()),
            ),
            Arguments.of(
                // Case where both lists are non-empty
                listOf(mockk<TopTrackInfo>()), listOf(mockk<TrackInfo>())
            )
        )
    }
}
