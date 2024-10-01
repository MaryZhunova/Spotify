package com.example.spotify.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.presentation.auth.AuthScreen
import com.example.spotify.presentation.artist.ArtistScreen
import com.example.spotify.presentation.top.TopArtistsScreen
import com.example.spotify.presentation.top.TopTracksScreen
import com.example.spotify.presentation.theme.SpotifyTheme
import com.example.spotify.presentation.auth.AuthViewModel
import com.example.spotify.presentation.top.TopGenresScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        authLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                authViewModel.handleAuthResult(result.resultCode, result.data)
            }

        setContent {
            SpotifyTheme {
                SpotifyApp(
                    authViewModel = authViewModel,
                    authLauncher = authLauncher,
                )
            }
        }
    }
}

@Composable
fun SpotifyApp(
    authViewModel: AuthViewModel,
    authLauncher: ActivityResultLauncher<Intent>,
) {
    val navController = rememberNavController()

    val isDark = isSystemInDarkTheme()
    var isStatusBarIconDark by remember { mutableStateOf(!isDark) }
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = isStatusBarIconDark
    )

    NavHost(navController = navController, startDestination = AUTH_SCREEN) {
        composable(AUTH_SCREEN) {
            AuthScreen(
                authViewModel = authViewModel,
                authLauncher = authLauncher,
                navController = navController
            )
        }
        composable(TOP_TRACKS_SCREEN) { TopTracksScreen(navController = navController) }
        composable(TOP_ARTISTS_SCREEN) { TopArtistsScreen(navController = navController) }
        composable(TOP_GENRES_SCREEN) { TopGenresScreen(navController = navController) }
        composable(
            route = "$ARTIST_SCREEN/{$ID_PARAM}",
            arguments = listOf(navArgument(ID_PARAM) {
                type = NavType.StringType
            })
        ) {
            it.arguments?.getString(ID_PARAM)?.let { id ->
                ArtistScreen(navController = navController, id = id) { isDark -> isStatusBarIconDark = isDark }
            }
        }
    }
}