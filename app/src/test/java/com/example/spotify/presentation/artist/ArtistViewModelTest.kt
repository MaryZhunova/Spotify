package com.example.spotify.presentation.artist

import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.ArtistScreenState
import com.example.spotify.utils.AudioPlayerManager
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    private val spotifyInteractor: SpotifyInteractor = mockk()
    private val audioPlayerManager: AudioPlayerManager = mockk()

    private lateinit var viewModel: ArtistViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ArtistViewModel(spotifyInteractor, audioPlayerManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ParameterizedTest(name = "{index} => topTracks={0}, favoriteTracks={1}")
    @MethodSource("provideTestData")
    fun fetchTracksAndArtistTest(
        topTracks: List<TrackInfo>,
        favoriteTracks: List<TrackInfo>
    ) = runTest {
        val artistId = "artist-id"
        val artistInfo = mockk<ArtistInfo>()
        coEvery { spotifyInteractor.getArtistsTopTracks(artistId) } returns topTracks
        coEvery { spotifyInteractor.getArtistsInfo(artistId) } returns artistInfo
        coEvery { spotifyInteractor.getTopTracksByArtistId(artistId) } returns favoriteTracks

        viewModel.fetchTracksAndArtist(artistId).join()

        assertThat(viewModel.topTracks.value).isEqualTo(topTracks)
        assertThat(viewModel.artist.value).isEqualTo(artistInfo)
        assertThat(viewModel.favoriteTracks.value).isEqualTo(favoriteTracks)
        assertThat(viewModel.state.value).isInstanceOf(ArtistScreenState.Success::class.java)

        coVerifySequence {
            spotifyInteractor.getArtistsTopTracks(artistId)
            spotifyInteractor.getArtistsInfo(artistId)
            spotifyInteractor.getTopTracksByArtistId(artistId)
        }
    }

    @Test
    fun `fetchTracksAndArtistTest error`() = runTest {
        val artistId = "artist-id"
        val error = Exception()
        coEvery { spotifyInteractor.getArtistsTopTracks(artistId) } throws error

        viewModel.fetchTracksAndArtist(artistId).join()

        assertThat(viewModel.topTracks.value).isEmpty()
        assertThat(viewModel.artist.value).isNull()
        assertThat(viewModel.favoriteTracks.value).isEmpty()
        assertThat(viewModel.state.value).isInstanceOf(ArtistScreenState.Fail::class.java)
        (viewModel.state.value as ArtistScreenState.Fail).apply {
            assertThat(this.error).isEqualTo(error)
        }

        coVerifySequence {
            spotifyInteractor.getArtistsTopTracks(artistId)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun changeIsHighlightedStateTest(isFav: Boolean) = runTest {
        val artistId = "artist-id"
        val artistInfo = mockk<ArtistInfo>()
        val topTracks = listOf(mockk<TrackInfo> {
            every { isFavorite } returns isFav
        })
        val favoriteTracks = listOf(mockk<TrackInfo>())
        coEvery { spotifyInteractor.getArtistsTopTracks(artistId) } returns topTracks
        coEvery { spotifyInteractor.getArtistsInfo(artistId) } returns artistInfo
        coEvery { spotifyInteractor.getTopTracksByArtistId(artistId) } returns favoriteTracks

        viewModel.fetchTracksAndArtist(artistId).join()

        assertThat(viewModel.changeIsHighlightedState()).isEqualTo(isFav)
        assertThat(viewModel.isFavoriteHighlighted.value).isEqualTo(isFav)

        assertThat(viewModel.changeIsHighlightedState()).isEqualTo(isFav)
        assertThat(viewModel.isFavoriteHighlighted.value).isFalse()
    }

    @Test
    fun playTest() {
        val trackInfo = mockk<TrackInfo>()
        every { audioPlayerManager.play(trackInfo) } just Runs

        viewModel.play(trackInfo)

        verify(exactly = 1) { audioPlayerManager.play(trackInfo) }
    }

    @Test
    fun stopTest() {
        every { audioPlayerManager.stop() } just Runs

        viewModel.stop()

        verify(exactly = 1) { audioPlayerManager.stop() }
    }

    @Test
    fun currentTrackTest() {
        val track = mockk<TrackInfo>()
        coEvery { audioPlayerManager.currentTrack } returns MutableStateFlow(track)

        assertThat(viewModel.currentTrack.value).isEqualTo(track)

        coVerify(exactly = 1) { audioPlayerManager.currentTrack }
    }

    companion object {
        @JvmStatic
        fun provideTestData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                // Case where both lists are empty
                listOf<TrackInfo>(), listOf<TrackInfo>()
            ),
            Arguments.of(
                // Case where topTracks is non-empty, favoriteTracks is empty
                listOf(mockk<TrackInfo>()), listOf<TrackInfo>()
            ),
            Arguments.of(
                // Case where topTracks is empty, favoriteTracks is non-empty
                listOf<TrackInfo>(), listOf(mockk<TrackInfo>()),
            ),
            Arguments.of(
                // Case where both lists are non-empty
                listOf(mockk<TrackInfo>()), listOf(mockk<TrackInfo>())
            )
        )
    }
}
