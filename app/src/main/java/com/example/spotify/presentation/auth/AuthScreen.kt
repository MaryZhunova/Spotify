package com.example.spotify.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.spotify.presentation.models.AuthState
import com.example.spotify.presentation.components.ProgressIndicator

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

    when (authState) {
        is AuthState.Idle -> AuthIdle { authViewModel.startAuth(activity, authLauncher) }
        is AuthState.Loading -> ProgressIndicator()
        is AuthState.Success -> AuthSuccess(
                onBackClick = { authViewModel.logout() },
                onTopClick = { navController.navigate(it) }
            )
        is AuthState.Fail ->  AuthFail(authState as AuthState.Fail) {
            authViewModel.startAuth(activity, authLauncher)
        }
    }
}