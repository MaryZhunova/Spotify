package com.example.spotify.presentation.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.spotify.models.presentation.AuthState
import com.example.spotify.presentation.viewmodels.AuthManager

@Composable
fun AuthScreen(
    authManager: AuthManager,
    navController: NavController,
) {
    when (authManager.authState.value) {
        is AuthState.Idle -> AuthIdle { authManager.startAuth() }
        is AuthState.Success -> AuthSuccess(
                onBackClick = { authManager.logout() },
                onTopClick = { navController.navigate(it) }
            )
        is AuthState.Fail ->  AuthFail(authManager.authState.value as AuthState.Fail) {
            authManager.startAuth()
        }
    }
}