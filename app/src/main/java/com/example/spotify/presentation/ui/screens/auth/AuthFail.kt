package com.example.spotify.presentation.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotify.models.presentation.AuthError
import com.example.spotify.models.presentation.AuthState


@Composable
 fun AuthFail(
    state: AuthState.Fail,
    tryAgain: () -> Unit
) {
    when (state.error) {
        AuthError.NO_SPOTIFY -> {
            Error {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = "Spotify not found"
                )
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = "You need to install Spotify first"
                )
            }
        }

        AuthError.AUTH_FAIL -> {
            Error {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = "Authentication failed"
                )
                Button(onClick = tryAgain) {
                    Text(
                        text = "Try again",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun Error(errorBlock: @Composable () -> Unit) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.background
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = colors,
                        center = Offset(size.width / 2, 0f),
                        radius = size.height * 0.6f
                    )
                )
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            fontSize = 46.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.surfaceVariant,
            text = "Oopsie..."
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            errorBlock.invoke()
        }
    }
}