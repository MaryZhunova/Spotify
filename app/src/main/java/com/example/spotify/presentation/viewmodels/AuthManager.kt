package com.example.spotify.presentation.viewmodels

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.spotify.BuildConfig
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthManager(
    private val activity: Activity,
    private val authLauncher: ActivityResultLauncher<Intent>
) {

    private val _accessToken = MutableStateFlow<AuthState>(AuthState.Idle)

    val accessToken: StateFlow<AuthState?>
        get() = _accessToken

    fun startAuth() {
        try {
            activity.packageManager.getPackageInfo("com.spotify.music", 0)

            val request = AuthorizationRequest.Builder(
                BuildConfig.CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI
            ).setScopes(arrayOf("user-read-private", "user-read-email")).build()
            val authIntent = AuthorizationClient.createLoginActivityIntent(activity, request)
            authLauncher.launch(authIntent)

        } catch (e: PackageManager.NameNotFoundException) {
            _accessToken.value = AuthState.Fail(error = AuthError.NO_SPOTIFY)
        }
    }

    fun handleAuthResult(resultCode: Int, data: Intent?) {
        val response = AuthorizationClient.getResponse(resultCode, data)
        if (response.type == AuthorizationResponse.Type.TOKEN) {
            _accessToken.value = AuthState.Success(accessToken = response.accessToken)
        } else {
            _accessToken.value = AuthState.Fail(error = AuthError.AUTH_FAIL)
        }
    }

    companion object {
        private const val REDIRECT_URI = "android-app://stats/auth"
        private const val STREAMING_SCOPE = "streaming"
    }
}


sealed interface AuthState {

    data object Idle: AuthState

    data class Fail(val error: AuthError): AuthState

    data class Success(val accessToken: String): AuthState
}


enum class AuthError {
    NO_SPOTIFY,
    AUTH_FAIL
}