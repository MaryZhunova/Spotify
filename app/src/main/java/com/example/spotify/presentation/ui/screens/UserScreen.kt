package com.example.spotify.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.presentation.viewmodels.UserViewModel

@Composable
fun UserScreen(accessToken: String) {
    val viewModel: UserViewModel = hiltViewModel()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(accessToken) {
        viewModel.loadUserProfile(accessToken)
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
