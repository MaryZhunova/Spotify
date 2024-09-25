package com.example.spotify.presentation.artist

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.components.PlayButton
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.components.ScrollableAppBar
import com.example.spotify.presentation.components.TrackItem
import kotlinx.coroutines.launch

/**
 * Экран с информацией об исполнителе
 *
 * @param navController контроллер навигации
 * @param id идентификатор исполнителя
 */
@Composable
fun ArtistScreen(
    navController: NavController,
    id: String
) {
    val viewModel: ArtistViewModel = hiltViewModel()

    val topTracks by viewModel.topTracks
    val artist by viewModel.artist
    val isLoading by viewModel.isLoading
    val isHighlighted by viewModel.isFavoriteHighlighted
    val favoriteTracks by viewModel.favoriteTracks
    val currentTrack by viewModel.currentTrack.collectAsState()

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
                WindowCompat.getInsetsController(it, it.decorView).isAppearanceLightStatusBars =
                    !isDark
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

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(top = maxOffsetHeightPx)
                .verticalScroll(scrollScope)
        ) {
            if (isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                ProgressIndicator()
            } else {
                if (favoriteTracks.isNotEmpty()) {
                    val snackbarMessage = stringResource(id = R.string.no_faves_snackbar)
                    var isSnackbarShowing by remember { mutableStateOf(false) }

                    TracksTabRow(
                        topTracks = topTracks,
                        favTracks = favoriteTracks,
                        currentTrack = currentTrack,
                        isHighlighted = isHighlighted,
                        onHighlightedChange = {
                            if (!viewModel.changeIsHighlightedState()) {
                                if (!isSnackbarShowing) {
                                    isSnackbarShowing = true
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(snackbarMessage).also {
                                            isSnackbarShowing = false
                                        }
                                    }
                                }
                            }
                        },
                        onPlay = { viewModel.play(it) },
                        onStop = { viewModel.stop() }
                    )
                } else {
                    TopTracks(
                        topTracks = topTracks,
                        currentTrack = currentTrack,
                        onPlay = { viewModel.play(it) },
                        onStop = { viewModel.stop() }
                    )
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
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun TracksTabRow(
    topTracks: List<TrackInfo>,
    favTracks: List<TrackInfo>,
    currentTrack: TrackInfo?,
    isHighlighted: Boolean,
    onHighlightedChange: () -> Unit,
    onPlay: (TrackInfo) -> Unit,
    onStop: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.tab_most_popular_tracks),
        stringResource(id = R.string.tab_faves)
    )
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { Text(title) }
            )
        }
    }

    when (selectedTabIndex) {
        0 -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHighlightedChange.invoke() }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(id = R.string.highlight),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            topTracks.forEach { track ->
                val isPlaying = currentTrack == track
                TrackItem(
                    track = track,
                    isHighlighted = isHighlighted,
                    isPlaying = isPlaying
                ) {
                    if (isPlaying) {
                        onStop.invoke()
                    } else {
                        onPlay.invoke(track)
                    }
                }
            }
        }

        1 -> {
            Spacer(modifier = Modifier.height(12.dp))
            favTracks.forEachIndexed { index, track ->
                val isPlaying = currentTrack == track
                TrackItem(track = track, index = index, isPlaying) {
                    if (isPlaying) {
                        onStop.invoke()
                    } else {
                        onPlay.invoke(track)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopTracks(
    topTracks: List<TrackInfo>,
    currentTrack: TrackInfo?,
    onPlay: (TrackInfo) -> Unit,
    onStop: () -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(id = R.string.tab_most_popular_tracks),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    topTracks.forEach { track ->
        val isPlaying = currentTrack == track
        TrackItem(track = track, isPlaying = isPlaying) {
            if (isPlaying) {
                onStop.invoke()
            } else {
                onPlay.invoke(track)
            }
        }
    }
}

@Composable
private fun TrackItem(
    track: TrackInfo,
    isHighlighted: Boolean = false,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val targetColor = if (isHighlighted && track.isFavorite) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.background
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500),
        label = "animatedColor"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(animatedColor)
            .padding(vertical = 4.dp).padding(start = 16.dp)
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
        if (!track.previewUrl.isNullOrBlank()) {
            PlayButton(
                isPlaying = isPlaying,
                onClick = onClick
            )
        }
    }
}