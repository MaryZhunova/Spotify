package com.example.spotify.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.presentation.ui.screens.auth.AuthScreen
import com.example.spotify.presentation.ui.screens.top.TopTracksScreen
import com.example.spotify.presentation.ui.theme.SpotifyTheme
import com.example.spotify.presentation.viewmodels.AuthManager
import com.example.spotify.presentation.viewmodels.SessionTimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var securityRepository: SecurityRepository
    @Inject
    lateinit var sessionTimerViewModel: SessionTimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        authLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                authManager.handleAuthResult(result.resultCode, result.data)
            }

        authManager = AuthManager(activity = this, authLauncher = authLauncher, securityRepository)

        sessionTimerViewModel.startTimer {
            authManager.logout()
        }

        setContent {
            SpotifyTheme {
                SpotifyApp(authManager)
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        sessionTimerViewModel.resetTimer()
    }
}

@Composable
fun SpotifyApp(authManager: AuthManager) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                authManager = authManager,
                navController = navController
            )
        }
        composable("tracks") { TopTracksScreen(navController = navController) }
        composable("artists") { TopTracksScreen(navController = navController) }
    }
}