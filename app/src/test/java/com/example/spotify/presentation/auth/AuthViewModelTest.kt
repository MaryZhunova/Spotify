package com.example.spotify.presentation.auth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import androidx.activity.result.ActivityResultLauncher
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.presentation.models.AuthError
import com.example.spotify.presentation.models.AuthState
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationRequest
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Тесты [AuthViewModel]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val authRepository: AuthRepository = mockk()
    private val infoRepository: SpotifyInfoRepository = mockk()
    private val activity: Activity = mockk()
    private val authLauncher: ActivityResultLauncher<Intent> = mockk()
    private val request = mockk<AuthorizationRequest>()

    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()

        every { authRepository.clear() } just Runs
        coEvery { infoRepository.clear() } just Runs
        viewModel = AuthViewModel(authRepository, infoRepository)

        mockkStatic(AuthorizationClient::createLoginActivityIntent)
        mockkConstructor(AuthorizationRequest.Builder::class)
        every { anyConstructed<AuthorizationRequest.Builder>().build() } returns request
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun initTest() = runTest {
        coVerifySequence {
            authRepository.clear()
            infoRepository.clear()
        }
        Truth.assertThat(viewModel.authState.value).isEqualTo(AuthState.Idle)
    }

    @Test
    fun `startAuth success`() = runTest {
        val authIntent = mockk<Intent>()

        every { activity.packageManager.getPackageInfo("com.spotify.music", 0) } returns mockk()
        every {
            AuthorizationClient.createLoginActivityIntent(
                activity,
                request
            )
        } returns authIntent
        every { authLauncher.launch(authIntent) } just Runs

        viewModel.startAuth(activity, authLauncher)

        verifySequence {
            activity.packageManager.getPackageInfo("com.spotify.music", 0)
            AuthorizationClient.createLoginActivityIntent(activity, request)
            authLauncher.launch(authIntent)
        }
    }

    @Test
    fun `startAuth fail NameNotFoundException`() = runTest {
        every {
            activity.packageManager.getPackageInfo(
                "com.spotify.music",
                0
            )
        } throws NameNotFoundException()

        viewModel.startAuth(activity, authLauncher)

        Truth.assertThat(viewModel.authState.value).isEqualTo(AuthState.Fail(AuthError.NO_SPOTIFY))

        verifySequence {
            activity.packageManager.getPackageInfo("com.spotify.music", 0)
        }
    }

    @Test
    fun `handleAuthResult success`() = runTest {
        val data = mockk<Intent>()
        val redirectUri = "android-app://stats/auth"
        val authCode = "auth-code"
        val response = mockk<AuthorizationResponse> {
            every { type } returns AuthorizationResponse.Type.CODE
            every { code } returns authCode
        }

        every { AuthorizationClient.getResponse(any(), any()) } returns response
        coEvery { authRepository.obtainAccessToken(authCode, redirectUri) } returns "token"

        viewModel.handleAuthResult(1, data).join()

        Truth.assertThat(viewModel.authState.value).isEqualTo(AuthState.Success)

        coVerify {
            AuthorizationClient.getResponse(1, data)
            authRepository.obtainAccessToken(authCode, redirectUri)
        }
    }

    @Test
    fun `handleAuthResult fail`() = runTest {
        val data = mockk<Intent>()
        val response = mockk<AuthorizationResponse> {
            every { type } returns AuthorizationResponse.Type.TOKEN
        }

        every { AuthorizationClient.getResponse(any(), any()) } returns response

        viewModel.handleAuthResult(1, data).join()

        Truth.assertThat(viewModel.authState.value).isEqualTo(AuthState.Fail(AuthError.AUTH_FAIL))

        coVerify(inverse = true) {
            authRepository.obtainAccessToken(any(), any())
        }
    }

    @Test
    fun `logout should clear repositories and set state to Idle`() = runTest(testDispatcher) {
        every { authRepository.clear() } just Runs
        coEvery { infoRepository.clear() } just Runs

        viewModel.logout().join()

        Truth.assertThat(viewModel.authState.value).isEqualTo(AuthState.Idle)

        coVerify {
            authRepository.clear()
            infoRepository.clear()
        }
    }
}
