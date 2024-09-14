package com.example.spotify.presentation.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spotify.R
import com.example.spotify.models.presentation.AuthError
import com.example.spotify.models.presentation.AuthState
import com.example.spotify.presentation.components.ErrorScreen

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
                    text = stringResource(id = R.string.spotify_not_found)
                )
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(id = R.string.install_spotify_first)
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
                    text = stringResource(id = R.string.auth_failed)
                )
                Button(onClick = tryAgain) {
                    Text(
                        text = stringResource(id = R.string.try_again),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}