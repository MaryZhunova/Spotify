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
import com.example.spotify.presentation.ui.screens.auth.AuthScreen
import com.example.spotify.presentation.ui.theme.SpotifyTheme
import com.example.spotify.presentation.viewmodels.AuthManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        authLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                authManager.handleAuthResult(result.resultCode, result.data)
            }

        authManager = AuthManager(activity = this, authLauncher = authLauncher)

        setContent {
            SpotifyTheme {
                SpotifyApp(authManager)
            }
        }
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
    }
}