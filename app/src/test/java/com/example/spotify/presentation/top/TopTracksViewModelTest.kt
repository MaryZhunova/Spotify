package com.example.spotify.presentation.top

import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.models.TopTracksState
import com.example.spotify.utils.AudioPlayerManager
import com.google.common.truth.Truth
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.jupiter.params.provider.CsvSource

/**
 * Тесты [TopTracksViewModel]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TopTracksViewModelTest {

    private val spotifyInteractor: SpotifyInteractor = mockk()
    private val audioPlayerManager: AudioPlayerManager = mockk()

    private val viewModel = TopTracksViewModel(spotifyInteractor, audioPlayerManager)

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchTopTracksTest() = runTest {
        val track = mockk<TrackInfo>()
        val info = listOf(track)
        coEvery { spotifyInteractor.getTopTracks("long_term") } returns info

        viewModel.fetchTopTracks(TimePeriods.LONG).join()

        Truth.assertThat(viewModel.topTracksState.value).isInstanceOf(TopTracksState.Success::class.java)
        (viewModel.topTracksState.value as TopTracksState.Success).also {
            Truth.assertThat(it.topTracks).isEqualTo(info)
        }

        viewModel.fetchTopTracks(TimePeriods.LONG).join()
        viewModel.fetchTopTracks(TimePeriods.LONG).join()

        coVerify(exactly = 1) {
            spotifyInteractor.getTopTracks("long_term")
        }
    }

    @Test
    fun `fetchTopTracksTest error`() = runTest {
        val error = Exception()
        coEvery { spotifyInteractor.getTopTracks("long_term") } throws error

        viewModel.fetchTopTracks(TimePeriods.LONG).join()

        Truth.assertThat(viewModel.topTracksState.value).isInstanceOf(TopTracksState.Fail::class.java)
        (viewModel.topTracksState.value as TopTracksState.Fail).also {
            Truth.assertThat(it.error).isEqualTo(error)
        }

        coVerify(exactly = 1) {
            spotifyInteractor.getTopTracks("long_term")
        }
    }

    @ParameterizedTest
    @CsvSource("SHORT", "MEDIUM", "LONG")
    fun switchSelected(period: TimePeriods) {

        viewModel.switchSelected(period)
        Truth.assertThat(viewModel.selectedPeriod.value).isEqualTo(period)
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

        Truth.assertThat(viewModel.currentTrack.value).isEqualTo(track)

        coVerify(exactly = 1) { audioPlayerManager.currentTrack }
    }
}