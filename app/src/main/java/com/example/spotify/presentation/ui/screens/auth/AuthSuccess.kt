package com.example.spotify.presentation.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.R
import com.example.spotify.data.security.NullAccessTokenException
import com.example.spotify.models.presentation.DialogState
import com.example.spotify.models.presentation.UserProfileState
import com.example.spotify.presentation.ui.components.ErrorScreen
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.ui.components.SimpleDialog
import com.example.spotify.presentation.ui.screens.UserProfileSuccessScreen
import com.example.spotify.presentation.viewmodels.AuthSuccessViewModel

/**
 * Компонент, отображающий экран успешной авторизации
 *
 * @param onBackClick Действие, выполняемое при нажатии на кнопку "Назад"
 * @param onTopClick Действие, выполняемое при нажатии на кнопки для перехода на экраны с топ-треков или топ-артистов
 */
@Composable
fun AuthSuccess(
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

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val profile = userProfile) {
            is UserProfileState.Idle -> ProgressIndicator()
            is UserProfileState.Success -> UserProfileSuccessScreen(
                info = profile.info,
                onBackClick = { viewModel.showExitDialog { onBackClick.invoke() } },
                onTopClick = { onTopClick.invoke(it) }
            )

            is UserProfileState.Error -> ErrorScreen {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
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