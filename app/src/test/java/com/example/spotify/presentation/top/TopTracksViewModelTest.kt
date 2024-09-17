package com.example.spotify.presentation.top

import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.models.TopTracksState
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val statsRepository: SpotifyUserStatsRepository = mockk()

    private val viewModel = TopTracksViewModel(statsRepository)

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
        coEvery { statsRepository.getTopTracks("long_term") } returns info

        viewModel.fetchTopTracks(TimePeriods.LONG).join()

        Truth.assertThat(viewModel.topTracksState.value).isInstanceOf(TopTracksState.Success::class.java)
        (viewModel.topTracksState.value as TopTracksState.Success).also {
            Truth.assertThat(it.topTracks).isEqualTo(info)
        }

        viewModel.fetchTopTracks(TimePeriods.LONG).join()
        viewModel.fetchTopTracks(TimePeriods.LONG).join()

        coVerify(exactly = 1) {
            statsRepository.getTopTracks("long_term")
        }
    }

    @Test
    fun `fetchTopTracksTest error`() = runTest {
        val error = Exception()
        coEvery { statsRepository.getTopTracks("long_term") } throws error

        viewModel.fetchTopTracks(TimePeriods.LONG).join()

        Truth.assertThat(viewModel.topTracksState.value).isInstanceOf(TopTracksState.Fail::class.java)
        (viewModel.topTracksState.value as TopTracksState.Fail).also {
            Truth.assertThat(it.error).isEqualTo(error)
        }

        coVerify(exactly = 1) {
            statsRepository.getTopTracks("long_term")
        }
    }

    @ParameterizedTest
    @CsvSource("SHORT", "MEDIUM", "LONG")
    fun switchSelected(period: TimePeriods) {

        viewModel.switchSelected(period)
        Truth.assertThat(viewModel.selectedPeriod.value).isEqualTo(period)

    }
}