package com.example.spotify.presentation.top

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ErrorIcon
import com.example.spotify.presentation.components.ProgressIndicator
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
        topBar = { AppBar { navController.popBackStack() } },
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(
                selectedTabIndex = periods.indexOf(selectedPeriod)
            ) {
                periods.forEach { period ->
                    Tab(
                        modifier = Modifier.padding(top = 16.dp),
                        selected = selectedPeriod == period,
                        onClick = { viewModel.switchSelected(period) }
                    ) {
                        Text(period.nameValue)
                    }
                }
            }

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
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            topTracksState.topTracks.forEachIndexed { index, track ->
                                val isPlaying = currentTrack == track
                                TrackItem(track = track, index = index, isPlaying = isPlaying) {
                                    if (isPlaying) {
                                        viewModel.stop()
                                    } else {
                                        viewModel.play(track)
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
