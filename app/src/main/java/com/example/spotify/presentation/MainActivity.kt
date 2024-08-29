package com.example.spotify.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotify.presentation.ui.screens.UserScreen
import com.example.spotify.presentation.ui.theme.SpotifyTheme
import com.example.spotify.presentation.viewmodels.AuthManager
import com.example.spotify.presentation.viewmodels.AuthState
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

        try {
            packageManager.getPackageInfo("com.spotify.music", 0)
            authManager.startAuth()
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "Install Spotify first", Toast.LENGTH_SHORT).show()
        }

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
    val accessToken by authManager.accessToken.collectAsState()

    NavHost(navController = navController, startDestination = "user") {
        composable("user") {
            when (accessToken) {
                is AuthState.Success -> {
                    (accessToken as? AuthState.Success)?.accessToken?.let { it1 -> UserScreen(accessToken = it1) }
                }
                else -> {}
            }
        }
    }
}