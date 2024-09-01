package com.example.spotify.presentation.ui.screens.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.presentation.viewmodels.TopTracksViewModel

@Composable
fun TopTracksScreen(
    navController: NavController,
) {
    val viewModel: TopTracksViewModel = hiltViewModel()

    val topTracks by viewModel.topTracks
    val isLoading by viewModel.isLoading

    // Create a lazy list state
    val listState = rememberLazyListState()

    // Fetch top tracks when the composable is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchTopTracks()
    }

    // Use derivedStateOf to avoid unnecessary recompositions
    val shouldFetchNextPage by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            !isLoading && lastVisibleItemIndex >= topTracks.size - 1
        }
    }

    // Fetch next page when the user scrolls to the end of the list
    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage) {
            viewModel.fetchNextPage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (isLoading && topTracks.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(state = listState) {
                items(items = topTracks, key = { it.id}) { track ->
                    TrackItem(track = track)
                }
                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
fun TrackItem(track: TrackInfo) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = track.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = track.artists.joinToString(", "),
            style = MaterialTheme.typography.bodySmall
        )
    }
}