package com.example.spotify.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.spotify.presentation.auth.success.AuthSuccessScreen
import com.example.spotify.presentation.models.AuthState

/**
 * Экран аутентификации
 *
 * @param navController контроллер навигации
 * @param authLauncher лаунчер для запуска активити
 * @param authViewModel вью модель, управляющая состоянием аутентификации
 */
@Composable
fun AuthScreen(
    navController: NavController,
    authLauncher: ActivityResultLauncher<Intent>,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState
    val activity = LocalContext.current as Activity

    Crossfade(targetState = authState, label = "crossfadeLabel") { state ->
        when (state) {
            is AuthState.Idle -> AuthIdle { authViewModel.startAuth(activity, authLauncher) }
            is AuthState.Success -> AuthSuccessScreen(
                onBackClick = { authViewModel.logout() },
                onTopClick = { navController.navigate(it) }
            )
            is AuthState.Fail -> AuthFail(
                state = state,
                onTryAgainClick = { authViewModel.startAuth(activity, authLauncher) },
                onBackClick = { authViewModel.logout() }
            )
            is AuthState.Loading -> authViewModel.checkIsAuthActual()
        }
    }
}