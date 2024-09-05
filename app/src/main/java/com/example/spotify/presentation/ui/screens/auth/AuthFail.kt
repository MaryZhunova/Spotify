package com.example.spotify.presentation.ui.screens.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spotify.models.presentation.AuthError
import com.example.spotify.models.presentation.AuthState
import com.example.spotify.presentation.ui.components.ErrorScreen

/**
 * Экран ошибки
 *
 * @param state состояние ошибки аутентификации, содержащее информацию об ошибке
 * @param tryAgain функция, вызываемая при попытке повторной аутентификации
 */
@Composable
 fun AuthFail(
    state: AuthState.Fail,
    tryAgain: () -> Unit
) {
    when (state.error) {
        AuthError.NO_SPOTIFY -> {
            ErrorScreen {
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
            ErrorScreen {
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