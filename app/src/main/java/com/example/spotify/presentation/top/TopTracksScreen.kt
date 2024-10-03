package com.example.spotify.presentation.top

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ErrorIcon
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.components.TabRow
import com.example.spotify.presentation.components.TrackItem
import com.example.spotify.presentation.models.TopTracksState

/**
 * Экран топа треков
 *
 * @param navController контроллер навигации
 */
@Composable
fun TopTracksScreen(
    navController: NavController
) {
    val viewModel: TopTracksViewModel = hiltViewModel()
    val periods = listOf(TimePeriods.SHORT, TimePeriods.MEDIUM, TimePeriods.LONG)

    val selectedPeriod by viewModel.selectedPeriod
    val state by viewModel.topTracksState
    val currentTrack by viewModel.currentTrack.collectAsState()

    LaunchedEffect(selectedPeriod) {
        viewModel.fetchTopTracks(selectedPeriod)
    }

    Scaffold(
        topBar = {
            AppBar(title = stringResource(id = R.string.top_tracks)) { navController.popBackStack() }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(
                selectedPeriod = selectedPeriod,
                periods = periods,
                onSwitch = { period -> viewModel.switchSelected(period) }
            )

            Crossfade(targetState = state, label = "crossfadeLabel") { topTracksState ->
                when (topTracksState) {
                    is TopTracksState.Idle -> {}
                    is TopTracksState.Loading -> ProgressIndicator()
                    is TopTracksState.Fail -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ErrorIcon()
                            Text(
                                modifier = Modifier.padding(bottom = 36.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.error,
                                text = topTracksState.error.message
                                    ?: stringResource(id = R.string.request_failed)
                            )
                        }
                    }

                    is TopTracksState.Success -> {
                        TracksList(
                            tracks = topTracksState.topTracks,
                            currentTrack = currentTrack,
                            onStop = { viewModel.stop() },
                            onPlay = { track -> viewModel.play(track) }
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun TracksList(
    tracks: List<TrackInfo>,
    currentTrack: TrackInfo?,
    onPlay: (TrackInfo) -> Unit,
    onStop: () -> Unit
) {
    var isFiltered by remember { mutableStateOf(false) }
    var displayedTracks by remember { mutableStateOf(tracks) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.filter),
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    displayedTracks = if (isFiltered) {
                        tracks
                    } else {
                        tracks.sortedByDescending { it.popularity }
                    }
                    isFiltered = !isFiltered
                }
                .padding(end = 16.dp),
            color = if (isFiltered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(displayedTracks.size) { index ->
                val isPlaying = currentTrack == displayedTracks[index]
                TrackItem(track = displayedTracks[index], index = index, isPlaying = isPlaying) {
                    if (isPlaying) {
                        onStop.invoke()
                    } else {
                        onPlay.invoke(displayedTracks[index])
                    }
                }
            }
        }
    }
}