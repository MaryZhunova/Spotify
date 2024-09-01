package com.example.spotify.presentation.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.presentation.ui.components.AppBar
import com.example.spotify.presentation.ui.components.ParallaxUserImage
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.viewmodels.AuthSuccessViewModel

@Composable
fun AuthSuccess(
    onBackClick: () -> Unit,
    onTopClick: (String) -> Unit
) {
    val viewModel: AuthSuccessViewModel = hiltViewModel()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userProfile?.let { profile ->
            AppBar(onBackClick)
            ParallaxUserImage(image = profile.image, name = profile.displayName)
            Text(
                modifier = Modifier.padding(top = 30.dp),
                fontSize = 46.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                text = profile.displayName
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Adds space between buttons
            ) {
                Button(onClick = { onTopClick.invoke("tracks") }) {
                    Text(text = "Top Tracks")
                }

                Button(onClick = { onTopClick.invoke("artists") }) {
                    Text(text = "Top Artists")
                }
            }
        } ?: ProgressIndicator()
    }
}