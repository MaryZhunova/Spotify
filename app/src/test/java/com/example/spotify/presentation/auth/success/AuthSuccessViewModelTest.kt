package com.example.spotify.presentation.auth.success

import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.models.UserProfileInfo
import com.example.spotify.presentation.models.DialogState
import com.example.spotify.presentation.models.UserProfileState
import com.google.common.truth.Truth.assertThat
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

/**
 * Тесты [AuthSuccessViewModel]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthSuccessViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val spotifyInteractor: SpotifyInteractor = mockk()
    private val viewModel = AuthSuccessViewModel(spotifyInteractor)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUserProfile successfully updates state`() = runTest {
        val userProfile = mockk<UserProfileInfo>()
        val userProfileState = UserProfileState.Success(userProfile)

        coEvery { spotifyInteractor.getCurrentUserProfile() } returns userProfile

        viewModel.loadUserProfile().join()

        assertThat(viewModel.userProfile.value).isEqualTo(userProfileState)

        coVerify {
            spotifyInteractor.getCurrentUserProfile()
        }
    }

    @Test
    fun `loadUserProfile handles error`() = runTest {
        val exception = RuntimeException("Error fetching user profile")
        val errorState = UserProfileState.Error(exception)

        coEvery { spotifyInteractor.getCurrentUserProfile() } throws exception

        viewModel.loadUserProfile().join()

        assertThat(viewModel.userProfile.value).isEqualTo(errorState)

        coVerify {
            spotifyInteractor.getCurrentUserProfile()
        }
    }

    @Test
    fun `showExitDialog updates dialog state and triggers onDismiss`() = runTest(testDispatcher) {
        val callback: () -> Unit = {}

        viewModel.showExitDialog(callback)

        assertThat(viewModel.dialogState.value).isInstanceOf(DialogState.Simple::class.java)
        (viewModel.dialogState.value as? DialogState.Simple)?.also { dialog ->
            assertThat(dialog.onNegative?.title).isEqualTo("Dismiss")
            assertThat(dialog.onPositive.title).isEqualTo("Confirm")
            assertThat(dialog.title).isEqualTo("Are you sure you want to exit?")

            dialog.onDismiss.invoke()
        }

        assertThat(viewModel.dialogState.value).isEqualTo(DialogState.Idle)
    }

    @Test
    fun `showExitDialog updates dialog state and triggers onNegative`() = runTest(testDispatcher) {
        val callback: () -> Unit = {}

        viewModel.showExitDialog(callback)

        assertThat(viewModel.dialogState.value).isInstanceOf(DialogState.Simple::class.java)
        (viewModel.dialogState.value as? DialogState.Simple)?.also { dialog ->
            assertThat(dialog.onNegative?.title).isEqualTo("Dismiss")
            assertThat(dialog.onPositive.title).isEqualTo("Confirm")
            assertThat(dialog.title).isEqualTo("Are you sure you want to exit?")

            dialog.onNegative?.action?.invoke()
        }

        assertThat(viewModel.dialogState.value).isEqualTo(DialogState.Idle)
    }

    @Test
    fun `showExitDialog updates dialog state and triggers onPositive`() = runTest(testDispatcher) {
        val callback: () -> Unit = {}

        viewModel.showExitDialog(callback)

        assertThat(viewModel.dialogState.value).isInstanceOf(DialogState.Simple::class.java)
        (viewModel.dialogState.value as? DialogState.Simple)?.also { dialog ->
            assertThat(dialog.onNegative?.title).isEqualTo("Dismiss")
            assertThat(dialog.onPositive.title).isEqualTo("Confirm")
            assertThat(dialog.title).isEqualTo("Are you sure you want to exit?")

            dialog.onPositive.action.invoke()
        }

        assertThat(viewModel.dialogState.value).isEqualTo(DialogState.Idle)
    }
}