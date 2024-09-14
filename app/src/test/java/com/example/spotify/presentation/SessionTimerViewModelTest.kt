package com.example.spotify.presentation

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

@ExperimentalCoroutinesApi
class SessionTimerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SessionTimerViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SessionTimerViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startTimer triggers session expiration after delay`() = runTest {
        val emissionCount = mutableListOf<Int>()

        val job = launch(testDispatcher) {
            viewModel.onSessionExpired.collect {
                emissionCount.add(1)
            }
        }

        viewModel.startTimer()
        testDispatcher.scheduler.advanceTimeBy(11.minutes.inWholeMilliseconds)
        assertThat(emissionCount).hasSize(1)
        job.cancel()

    }

    @Test
    fun `resetTimer cancels the existing timer and starts a new one`() = runTest {

        val emissionCount = mutableListOf<Int>()
        val job = launch(testDispatcher) {
            viewModel.onSessionExpired.collect {
                emissionCount.add(1)
            }
        }

        viewModel.startTimer()
        val initialJob = viewModel.timerJob
        testDispatcher.scheduler.advanceTimeBy(5.minutes.inWholeMilliseconds)
        viewModel.resetTimer()
        val newJob = viewModel.timerJob
        testDispatcher.scheduler.advanceTimeBy(11.minutes.inWholeMilliseconds)

        assertThat(emissionCount.size).isEqualTo(1)
        job.cancel()
        assertThat(initialJob).isNotSameInstanceAs(newJob)
        assertThat(viewModel.timerJob?.isCompleted).isTrue()
    }

    @Test
    fun `startTimer does not restart if already active`() = runTest {

        viewModel.startTimer()
        val initialJob = viewModel.timerJob

        viewModel.startTimer()

        assertThat(viewModel.timerJob).isEqualTo(initialJob)
        assertThat(viewModel.timerJob?.isActive).isTrue()
    }
}
