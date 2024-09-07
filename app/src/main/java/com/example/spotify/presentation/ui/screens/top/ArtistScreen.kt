package com.example.spotify.presentation.ui.screens.top

import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.ui.components.ScrollableAppBar
import com.example.spotify.presentation.viewmodels.ArtistViewModel

@Composable
fun ArtistScreen(
    navController: NavController,
    id: String
) {
    val viewModel: ArtistViewModel = hiltViewModel()

    val topTracks by viewModel.topTracks
    val artist by viewModel.artist
    val isLoading by viewModel.isLoading

    LaunchedEffect(id) {
        viewModel.fetchTracksAndArtist(id)
    }
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val window = activity?.window

    val isDark = isSystemInDarkTheme()

    DisposableEffect(Unit) {
        window?.let {
            WindowCompat.getInsetsController(it, it.decorView).apply {
                isAppearanceLightStatusBars = false
            }
        }
        onDispose {
            window?.let {
                WindowCompat.getInsetsController(it, it.decorView).isAppearanceLightStatusBars = !isDark
            }
        }
    }

    val toolbarHeight = 250.dp

    val maxUpPx = with(LocalDensity.current) {
        toolbarHeight.roundToPx().toFloat() - 100.dp.roundToPx().toFloat()
    }
    val minUpPx = 0f
    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val scrollScope = rememberScrollState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-maxUpPx, minUpPx)
                return Offset.Zero
            }
        }
    }
    val maxOffsetHeightPx = with(LocalDensity.current) {
        toolbarHeight + toolbarOffsetHeightPx.floatValue.toDp()
    }
    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(modifier = Modifier
            .padding(top = maxOffsetHeightPx)
            .verticalScroll(scrollScope)) {
            if (isLoading) {
                ProgressIndicator()
            } else {
                Text(
                    modifier = Modifier
                        .padding(20.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    text = "Top Tracks",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                topTracks.forEach { track ->
                    TrackItem(track = track)
                }
            }
        }
        ScrollableAppBar(
            title = artist?.name.orEmpty(),
            backgroundImage = artist?.image.orEmpty(),
            scrollableAppBarHeight = toolbarHeight,
            toolbarOffsetHeightPx = toolbarOffsetHeightPx,
            onClick = { navController.popBackStack() }
        )
    }

}

@Composable
private fun TrackItem(track: TrackInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.album.image)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.music_icon),
            error = painterResource(R.drawable.music_icon),
            alignment = Alignment.CenterStart,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
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