package com.example.spotify.presentation.playlist

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.models.CustomPlaylistScreenState
import com.example.spotify.presentation.top.TracksList

@Composable
fun PlaylistScreen(navController: NavController) {

    val viewModel: CustomPlaylistViewModel = hiltViewModel()

    val state by viewModel.state

    Crossfade(targetState = state, label = "label") { screenState ->
        when (screenState) {
            is CustomPlaylistScreenState.Idle -> {
                AppBar(title = stringResource(id = R.string.custom_playlist)) { navController.popBackStack() }
                viewModel.init()
            }

            is CustomPlaylistScreenState.Loading -> {
                AppBar(title = stringResource(id = R.string.custom_playlist)) { navController.popBackStack() }
                ProgressIndicator()
            }

            is CustomPlaylistScreenState.PlaylistCreating -> {}
            is CustomPlaylistScreenState.PlaylistCreated -> {
                Scaffold(
                    topBar = {
                        AppBar(title = stringResource(id = R.string.custom_playlist)) { navController.popBackStack() }
                    }
                ) {
                    Column(modifier = Modifier.padding(it)) {
                        TracksList(
                            tracks = screenState.playlist,
                            currentTrack = screenState.currentTrack.collectAsState().value,
                            onStop = screenState.onStop,
                            onPlay = { track -> screenState.onPlay(track) }
                        )
                    }
                }
            }

            is CustomPlaylistScreenState.Fail -> {}
            is CustomPlaylistScreenState.Success -> PlaylistSuccessScreen(
                state = screenState,
                onBackClick = { navController.popBackStack() },
                onCreatePlaylist = { viewModel.createPlaylist() }
            )
        }
    }
}