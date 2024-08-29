package com.example.spotify.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.spotify.BuildConfig
import com.example.spotify.R
import com.example.spotify.presentation.ui.screens.AuthScreen
import com.example.spotify.presentation.ui.theme.SpotifyTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    private val spotifyAuthLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleAuthResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SpotifyTheme {
                AuthScreen {
                    val builder = AuthorizationRequest.Builder(
                        BuildConfig.CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI
                    ).setScopes(arrayOf(STREAMING_SCOPE))

                    val request = builder.build()
                    val authIntent = AuthorizationClient.createLoginActivityIntent(this, request)
                    spotifyAuthLauncher.launch(authIntent)
                }
            }
        }
    }

    private fun handleAuthResult(result: ActivityResult) {
        val response = AuthorizationClient.getResponse(result.resultCode, result.data)
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
            }

            AuthorizationResponse.Type.ERROR -> {
                Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    companion object {
        private const val REDIRECT_URI = "android-app://stats/auth"
        private const val STREAMING_SCOPE = "streaming"
    }
}
