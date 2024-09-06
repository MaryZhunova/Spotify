package com.example.spotify.presentation.ui.screens.auth

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotify.data.security.NullAccessTokenException
import com.example.spotify.models.data.UserProfileInfo
import com.example.spotify.models.presentation.DialogState
import com.example.spotify.models.presentation.UserProfileState
import com.example.spotify.presentation.TOP_ARTISTS_SCREEN
import com.example.spotify.presentation.TOP_TRACKS_SCREEN
import com.example.spotify.presentation.ui.components.AppBar
import com.example.spotify.presentation.ui.components.ErrorScreen
import com.example.spotify.presentation.ui.components.ParallaxUserImage
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.ui.components.SimpleDialog
import com.example.spotify.presentation.ui.components.UserImage
import com.example.spotify.presentation.viewmodels.AuthSuccessViewModel

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
                    text = "Couldn't retrieve the data"
                )
                if (profile.err is NullAccessTokenException) {
                    Button(onClick = {
                        onBackClick.invoke()
                    }) {
                        Text(
                            text = "Log in again",
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

@Composable
fun UserProfileSuccessScreen(
    info: UserProfileInfo, onBackClick: () -> Unit, onTopClick: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    AppBar { onBackClick.invoke() }
    if (configuration.orientation == ORIENTATION_PORTRAIT) {
        ParallaxUserImage(image = info.image, name = info.displayName)
        Text(
            modifier = Modifier.padding(top = 40.dp),
            fontSize = 46.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = info.displayName
        )
    } else {
        UserImage(
            modifier = Modifier.offset(y = (-40).dp), image = info.image, name = info.displayName
        )
        Text(
            fontSize = 46.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = info.displayName
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { onTopClick.invoke(TOP_TRACKS_SCREEN) }) {
            Text(text = "Top Tracks")
        }

        Button(onClick = { onTopClick.invoke(TOP_ARTISTS_SCREEN) }) {
            Text(text = "Top Artists")
        }
    }
}