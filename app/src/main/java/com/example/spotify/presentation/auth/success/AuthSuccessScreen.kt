package com.example.spotify.presentation.auth.success

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.R
import com.example.spotify.data.auth.NullAccessTokenException
import com.example.spotify.presentation.models.DialogState
import com.example.spotify.presentation.models.UserProfileState
import com.example.spotify.presentation.components.ErrorScreen
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.components.SimpleDialog

/**
 * Компонент, отображающий экран успешной авторизации
 *
 * @param onBackClick Действие, выполняемое при нажатии на кнопку "Назад"
 * @param onTopClick Действие, выполняемое при нажатии на кнопки для перехода на экраны с топ-треков или топ-артистов
 */
@Composable
fun AuthSuccessScreen(
    onBackClick: () -> Unit,
    onTopClick: (String) -> Unit,
) {
    val viewModel: AuthSuccessViewModel = hiltViewModel()
    val dialogState by viewModel.dialogState.collectAsState()
    val userProfile by viewModel.userProfile

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    BackHandler {
        viewModel.showExitDialog { onBackClick.invoke() }
    }

    Crossfade(targetState = userProfile, label = "crossfadeLabel") { profile ->
        when (profile) {
            is UserProfileState.Idle -> ProgressIndicator()
            is UserProfileState.Success -> AuthSuccess(
                info = profile.info,
                onBackClick = { viewModel.showExitDialog { onBackClick.invoke() } },
                onTopClick = { onTopClick.invoke(it) }
            )

            is UserProfileState.Error -> ErrorScreen {
                Text(
                    modifier = Modifier.padding(bottom = 36.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(id = R.string.no_data)
                )
                if (profile.err is NullAccessTokenException) {
                    Button(onClick = {
                        onBackClick.invoke()
                    }) {
                        Text(
                            text = stringResource(id = R.string.log_in_again),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }

    when (val state = dialogState) {
        is DialogState.Simple -> SimpleDialog(data = state)
        is DialogState.Idle -> {}
    }
}