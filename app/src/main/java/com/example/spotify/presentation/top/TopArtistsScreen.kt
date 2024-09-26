package com.example.spotify.presentation.top

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.ARTIST_SCREEN
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ErrorIcon
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.components.TabRow
import com.example.spotify.presentation.models.TopArtistsState

/**
 * Экран топа исполнителей
 *
 * @param navController контроллер навигации
 */
@Composable
fun TopArtistsScreen(
    navController: NavController
) {
    val viewModel: TopArtistsViewModel = hiltViewModel()

    val periods = listOf(TimePeriods.SHORT, TimePeriods.MEDIUM, TimePeriods.LONG)
    val selectedPeriod by viewModel.selectedPeriod

    val state by viewModel.topArtistsState

    LaunchedEffect(selectedPeriod) {
        viewModel.fetchTopArtists(selectedPeriod)
    }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.fav_artists)
            ) { navController.popBackStack() }
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(
                selectedPeriod = selectedPeriod,
                periods = periods,
                onSwitch = { period -> viewModel.switchSelected(period) }
            )
            Crossfade(targetState = state, label = "crossfadeLabel") { topArtistsState ->
                when (topArtistsState) {
                    is TopArtistsState.Idle -> {}
                    is TopArtistsState.Loading -> ProgressIndicator()
                    is TopArtistsState.Fail -> {
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
                                text = topArtistsState.error.message
                                    ?: stringResource(id = R.string.request_failed)
                            )
                        }
                    }

                    is TopArtistsState.Success -> {
                        FavoriteArtistsList(
                            topArtistsState.topArtists
                        ) { id -> navController.navigate("$ARTIST_SCREEN/$id") }
                    }

                }
            }
        }
    }
}

@Composable
fun FavoriteArtistsList(artists: List<ArtistInfo>, onClick: (String) -> Unit) {
    var isFiltered by remember { mutableStateOf(false) }
    var displayedArtists by remember { mutableStateOf(artists) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.filter),
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    displayedArtists = if (isFiltered) {
                        artists
                    } else {
                        artists.sortedByDescending { it.popularity }
                    }
                    isFiltered = !isFiltered
                },
            color = if (isFiltered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayedArtists.size) { index ->
                ArtistItem(artist = displayedArtists[index], index = index + 1) { id ->
                    onClick.invoke(id)

                }
            }
        }
    }
}

@Composable
private fun ArtistItem(artist: ArtistInfo, index: Int, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(artist.id) },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
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
                    .padding(end = 8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Popularity: ${artist.popularity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Genres: ${artist.genres}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}