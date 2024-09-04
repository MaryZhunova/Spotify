package com.example.spotify.presentation.ui.screens.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.presentation.TimePeriods
import com.example.spotify.presentation.ui.components.AppBar
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.viewmodels.TopArtistsViewModel

/**
 * Экран топа исполнителей
 *
 * @param navController контроллер навигации
 */
@Composable
fun TopArtistsScreen(
    navController: NavController,
) {
    val viewModel: TopArtistsViewModel = hiltViewModel()
    var selectedPeriod by remember { mutableStateOf(TimePeriods.SHORT) }

    val topArtists by viewModel.topArtists
    val isLoading by viewModel.isLoading

    LaunchedEffect(selectedPeriod) {
        viewModel.fetchTopArtists(selectedPeriod)
    }

    Scaffold(
        topBar = { AppBar { navController.popBackStack() } },
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(
                selectedTabIndex = when (selectedPeriod) {
                    TimePeriods.SHORT -> 0
                    TimePeriods.MEDIUM -> 1
                    TimePeriods.LONG -> 2
                }
            ) {
                Tab(
                    modifier = Modifier.padding(top = 16.dp),
                    selected = selectedPeriod == TimePeriods.SHORT,
                    onClick = { selectedPeriod = TimePeriods.SHORT }) {
                    Text("4 weeks")
                }
                Tab(
                    modifier = Modifier.padding(top = 16.dp),
                    selected = selectedPeriod == TimePeriods.MEDIUM,
                    onClick = { selectedPeriod = TimePeriods.MEDIUM }) {
                    Text("6 months")
                }
                Tab(
                    modifier = Modifier.padding(top = 16.dp),
                    selected = selectedPeriod == TimePeriods.LONG,
                    onClick = { selectedPeriod = TimePeriods.LONG }) {
                    Text("12 months")
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
                    topArtists.forEachIndexed { index, artistInfo ->
                        ArtistItem(artist = artistInfo, index = index)
                    }
                    if (isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ArtistItem(artist: ArtistInfo, index: Int) {
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
                .data(artist.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.music_icon),
            error = painterResource(R.drawable.music_icon),
            alignment = Alignment.CenterStart,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(10.dp))
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
                text = artist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall,
                text = artist.genres,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}