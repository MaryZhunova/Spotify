package com.example.spotify.presentation.viewmodels

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.spotify.BuildConfig
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.presentation.AuthError
import com.example.spotify.models.presentation.AuthState
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthManager(
    private val activity: Activity,
    private val authLauncher: ActivityResultLauncher<Intent>,
    private val securityRepository: SecurityRepository
) {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)

    val authState: State<AuthState>
        get() = _authState

    fun startAuth() {
        try {
            activity.packageManager.getPackageInfo(PACKAGE_NAME, 0)

            val request = AuthorizationRequest.Builder(
                BuildConfig.CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI
            ).setScopes(arrayOf(USER_READ_PRIVATE, USER_READ_EMAIL, USER_TOP_READ)).build()
            val authIntent = AuthorizationClient.createLoginActivityIntent(activity, request)
            authLauncher.launch(authIntent)

        } catch (e: PackageManager.NameNotFoundException) {
            _authState.value = AuthState.Fail(error = AuthError.NO_SPOTIFY)
        }
    }

    fun handleAuthResult(resultCode: Int, data: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = AuthorizationClient.getResponse(resultCode, data)
            if (response.type == AuthorizationResponse.Type.CODE &&
                securityRepository.obtainAccessToken(response.code, REDIRECT_URI) != null
            ) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Fail(error = AuthError.AUTH_FAIL)
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Idle
        AuthorizationClient.clearCookies(activity)
    }

    companion object {
        private const val REDIRECT_URI = "android-app://stats/auth"
        private const val PACKAGE_NAME = "com.spotify.music"
        private const val USER_READ_PRIVATE = "user-read-private"
        private const val USER_READ_EMAIL = "user-read-email"
        private const val USER_TOP_READ = "user-top-read"
    }
}