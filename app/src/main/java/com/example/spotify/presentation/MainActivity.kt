package com.example.spotify.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.presentation.ui.screens.auth.AuthScreen
import com.example.spotify.presentation.ui.screens.top.TopArtistsScreen
import com.example.spotify.presentation.ui.screens.top.TopTracksScreen
import com.example.spotify.presentation.ui.theme.SpotifyTheme
import com.example.spotify.presentation.viewmodels.AuthViewModel
import com.example.spotify.presentation.viewmodels.SessionTimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sessionTimerViewModel: SessionTimerViewModel by viewModels()

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var securityRepository: SecurityRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        authLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                authViewModel.handleAuthResult(result.resultCode, result.data)
            }

        sessionTimerViewModel.startTimer()

        setContent {
            SpotifyTheme {
                SpotifyApp(
                    authViewModel = authViewModel,
                    authLauncher = authLauncher,
                    sessionTimerViewModel = sessionTimerViewModel
                )
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        sessionTimerViewModel.resetTimer()
    }
}

@Composable
fun SpotifyApp(
    authViewModel: AuthViewModel,
    authLauncher: ActivityResultLauncher<Intent>,
    sessionTimerViewModel: SessionTimerViewModel
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        sessionTimerViewModel.onSessionExpired.collect {
            authViewModel.logout()
            navController.popBackStack("auth", false)
        }
    }

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                authViewModel = authViewModel,
                authLauncher = authLauncher,
                navController = navController
            )
        }
        composable("tracks") { TopTracksScreen(navController = navController) }
        composable("artists") { TopArtistsScreen(navController = navController) }
    }
}