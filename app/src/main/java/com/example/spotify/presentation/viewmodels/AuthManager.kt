package com.example.spotify.presentation.viewmodels

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import com.example.spotify.BuildConfig
import com.example.spotify.models.presentation.AuthError
import com.example.spotify.models.presentation.AuthState
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthManager(
    private val activity: Activity,
    private val authLauncher: ActivityResultLauncher<Intent>
) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    val authState: StateFlow<AuthState>
        get() = _authState

    init {
        try {
            activity.packageManager.getPackageInfo("com.spotify.music", 0)

            val request = AuthorizationRequest.Builder(
                BuildConfig.CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI
            ).setScopes(arrayOf("user-read-private", "user-read-email")).build()
            val authIntent = AuthorizationClient.createLoginActivityIntent(activity, request)
            authLauncher.launch(authIntent)

        } catch (e: PackageManager.NameNotFoundException) {
            _authState.value = AuthState.Fail(error = AuthError.NO_SPOTIFY)
        }
    }

    fun handleAuthResult(resultCode: Int, data: Intent?) {
        val response = AuthorizationClient.getResponse(resultCode, data)
        if (response.type == AuthorizationResponse.Type.TOKEN) {
            _authState.value = AuthState.Success(accessToken = response.accessToken)
        } else {
            _authState.value = AuthState.Fail(error = AuthError.AUTH_FAIL)
        }
    }

    companion object {
        private const val REDIRECT_URI = "android-app://stats/auth"
        private const val STREAMING_SCOPE = "streaming"
    }
}