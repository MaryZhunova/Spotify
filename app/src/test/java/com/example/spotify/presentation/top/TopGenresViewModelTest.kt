package com.example.spotify.presentation.top

import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.models.TopGenresState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Тесты [TopGenresViewModel]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TopGenresViewModelTest {

    private lateinit var spotifyInteractor: SpotifyInteractor
    private lateinit var viewModel: TopGenresViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        spotifyInteractor = mockk()
        viewModel = TopGenresViewModel(spotifyInteractor)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchTopGenres should return success state when genres are fetched`() = runTest {
        val genreInfo = mockk<GenreInfo>()
        val info = listOf(genreInfo)
        val period = TimePeriods.SHORT

        coEvery { spotifyInteractor.getTopGenres(period.strValue) } returns info

        viewModel.fetchTopGenres(period)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.topGenresState.value).isInstanceOf(TopGenresState.Success::class.java)
        (viewModel.topGenresState.value as TopGenresState.Success).also {
            assertThat(it.genreInfos).isEqualTo(info)
        }
    }

    @Test
    fun `fetchTopGenres should return cached genres if already fetched`() = runTest {
        val genreInfo = mockk<GenreInfo>()
        val info = listOf(genreInfo)
        val period = TimePeriods.SHORT

        coEvery { spotifyInteractor.getTopGenres(period.strValue) } returns info
        viewModel.fetchTopGenres(period)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.fetchTopGenres(period)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { spotifyInteractor.getTopGenres(period.strValue) }

        assertThat(viewModel.topGenresState.value).isInstanceOf(TopGenresState.Success::class.java)
        (viewModel.topGenresState.value as TopGenresState.Success).also {
            assertThat(it.genreInfos).isEqualTo(info)
        }
    }

    @Test
    fun `fetchTopGenres should handle error state correctly`() = runTest {
        val error = Exception("Network Error")
        val period = TimePeriods.SHORT

        coEvery { spotifyInteractor.getTopGenres(period.strValue) } throws error

        viewModel.fetchTopGenres(period)

        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.topGenresState.value).isInstanceOf(TopGenresState.Fail::class.java)
        (viewModel.topGenresState.value as TopGenresState.Fail).also {
            assertThat(it.error).isEqualTo(error)
        }

        coVerify(exactly = 1) { spotifyInteractor.getTopGenres(period.strValue) }
    }

    @ParameterizedTest
    @CsvSource("SHORT", "MEDIUM", "LONG")
    fun `switchSelected should update the selected period`(period: TimePeriods) {
        viewModel.switchSelected(period)
        assertThat(viewModel.selectedPeriod.value).isEqualTo(period)
    }
}