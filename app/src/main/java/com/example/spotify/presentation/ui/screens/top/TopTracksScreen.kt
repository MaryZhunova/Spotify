package com.example.spotify.presentation.ui.screens.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.models.presentation.TimePeriods
import com.example.spotify.presentation.ui.components.AppBar
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.viewmodels.TopTracksViewModel

/**
 * Экран топа треков
 *
 * @param navController контроллер навигации
 */
@Composable
fun TopTracksScreen(
    navController: NavController,
) {
    val viewModel: TopTracksViewModel = hiltViewModel()
    val periods = listOf(TimePeriods.SHORT, TimePeriods.MEDIUM, TimePeriods.LONG)
    val selectedPeriod by viewModel.selectedPeriod


    val topTracks by viewModel.topTracks
    val isLoading by viewModel.isLoading

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
            if (isLoading) {
                ProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    topTracks.forEachIndexed { index, track ->
                        TrackItem(track = track, index = index)
                    }


                }
            }
        }
    }
}

@Composable
fun TrackItem(track: TrackInfo, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            text = "${index + 1}.",
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.album.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.music_icon),
            error = painterResource(R.drawable.music_icon),
            alignment = Alignment.CenterStart,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(45.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                text = track.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall,
                text = track.artists,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}