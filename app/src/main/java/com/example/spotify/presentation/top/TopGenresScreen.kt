package com.example.spotify.presentation.top

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ErrorIcon
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.components.TabRow
import com.example.spotify.presentation.models.TopGenresState

/**
 * Экран топа жанров
 *
 * @param navController контроллер навигации
 */
@Composable
fun TopGenresScreen(
    navController: NavController
) {
    val viewModel: TopGenresViewModel = hiltViewModel()
    val periods = listOf(TimePeriods.SHORT, TimePeriods.MEDIUM, TimePeriods.LONG)

    val selectedPeriod by viewModel.selectedPeriod
    val state by viewModel.topGenresState

    LaunchedEffect(selectedPeriod) {
        viewModel.fetchTopGenres(selectedPeriod)
    }

    Scaffold(
        topBar = {
            AppBar(title = stringResource(id = R.string.top_genres)) { navController.popBackStack() }
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(
                selectedPeriod = selectedPeriod,
                periods = periods,
                onSwitch = { period -> viewModel.switchSelected(period) }
            )
            Crossfade(targetState = state, label = "crossfadeLabel") { topTracksState ->
                when (topTracksState) {
                    is TopGenresState.Idle -> {}
                    is TopGenresState.Loading -> ProgressIndicator()
                    is TopGenresState.Fail -> {
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
                    is TopGenresState.Success -> {
                        TopGenresList(topTracksState.genreInfos)
                    }
                }

            }
        }
    }
}

@Composable
fun TopGenresList(genreInfos: List<GenreInfo>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(genreInfos) { topGenre ->
                GenreItem(topGenre)
            }
        }
    }
}

@Composable
private fun GenreItem(genreInfo: GenreInfo) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { isExpanded = !isExpanded }, // Toggle expansion on click
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = genreInfo.genre,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "${genreInfo.numberOfArtists} Artists",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Artists: ${genreInfo.artistNames.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2
            )
        }
    }
}
