package com.example.spotify.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.models.presentation.AuthError
import com.example.spotify.models.presentation.AuthState
import com.example.spotify.presentation.viewmodels.UserViewModel

@Composable
fun UserScreen(authState: State<AuthState>) {
    when (authState.value) {
        is AuthState.Success -> AuthSuccess(authState.value as AuthState.Success)
        is AuthState.Fail -> AuthFail(authState.value as AuthState.Fail)
        else -> {}
    }

}

@Composable
private fun AuthSuccess(state: AuthState.Success) {
    val viewModel: UserViewModel = hiltViewModel()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(state.accessToken) {
        viewModel.loadUserProfile(state.accessToken)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (userProfile != null) {
            Text("User Name: ${userProfile?.displayName}")
        } else {
            //todo user not found
            CircularProgressIndicator()
        }
    }
}
@Composable
private fun AuthFail(state: AuthState.Fail) {
    when (state.error) {
        AuthError.NO_SPOTIFY -> {}
        AuthError.AUTH_FAIL -> {}
    }
}
