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
import com.example.spotify.presentation.models.AuthError
import com.example.spotify.presentation.models.AuthState
import com.example.spotify.presentation.components.ErrorScreen

/**
 * Экран ошибки
 *
 * @param state состояние ошибки аутентификации, содержащее информацию об ошибке
 * @param onTryAgainClick функция, вызываемая при попытке повторной аутентификации
 * @param onBackClick функция возврата на стартовый экран
 */
@Composable
 fun AuthFail(
    state: AuthState.Fail,
    onTryAgainClick: () -> Unit,
    onBackClick: () -> Unit
) {
    when (state.error) {
        AuthError.NO_SPOTIFY -> {
            ErrorScreen {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(id = R.string.spotify_not_found)
                )
                Text(
                    modifier = Modifier.padding(bottom = 36.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(id = R.string.install_spotify_first)
                )
                Button(onClick = onBackClick) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        AuthError.AUTH_FAIL -> {
            ErrorScreen {
                Text(
                    modifier = Modifier.padding(bottom = 36.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(id = R.string.auth_failed)
                )
                Button(onClick = onTryAgainClick) {
                    Text(
                        text = stringResource(id = R.string.try_again),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}