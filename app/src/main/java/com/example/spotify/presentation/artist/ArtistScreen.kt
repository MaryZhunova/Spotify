package com.example.spotify.presentation.artist

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotify.R
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ErrorIcon
import com.example.spotify.presentation.components.ProgressIndicator
import com.example.spotify.presentation.components.ScrollableAppBar
import com.example.spotify.presentation.components.TrackItem
import com.example.spotify.presentation.models.ArtistScreenState
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
    id: String,
    changeStatusBarIconColor: (Boolean) -> Unit
) {
    val viewModel: ArtistViewModel = hiltViewModel()

    val state by viewModel.state
    val topTracks by viewModel.topTracks
    val artist by viewModel.artist
    val isHighlighted by viewModel.isFavoriteHighlighted
    val favoriteTracks by viewModel.favoriteTracks
    val currentTrack by viewModel.currentTrack.collectAsState()

    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        viewModel.fetchTracksAndArtist(id)
    }

    DisposableEffect(navController.currentBackStackEntry) {
        onDispose { viewModel.reset() }
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
            .navigationBarsPadding()
    ) {
        Crossfade(targetState = state, label = "label") { screenState ->
            when (screenState) {
                is ArtistScreenState.Idle -> {
                    changeStatusBarIconColor.invoke(!isDark)
                }
                is ArtistScreenState.Loading -> {
                    changeStatusBarIconColor.invoke(!isDark)
                    AppBar(bgColor = MaterialTheme.colorScheme.background) { navController.popBackStack() }
                    Spacer(modifier = Modifier.height(12.dp))
                    ProgressIndicator()
                }
                is ArtistScreenState.Fail -> {
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
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            text = screenState.error.message
                                ?: stringResource(id = R.string.request_failed)
                        )
                    }
                    AppBar(bgColor = MaterialTheme.colorScheme.background) { navController.popBackStack() }
                }
                is ArtistScreenState.Success -> {
                    changeStatusBarIconColor.invoke(false)
                    Column(
                        modifier = Modifier
                            .padding(top = maxOffsetHeightPx)
                            .verticalScroll(scrollScope)
                    ) {
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
                    ScrollableAppBar(
                        title = artist?.name.orEmpty(),
                        backgroundImage = artist?.image.orEmpty(),
                        scrollableAppBarHeight = toolbarHeight,
                        toolbarOffsetHeightPx = toolbarOffsetHeightPx,
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
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
                text = { Text(title) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface
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
            FavTracks(
                tracks = favTracks,
                currentTrack = currentTrack,
                onPlay = { onPlay.invoke(it) },
                onStop = { onStop.invoke() }
            )
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
private fun FavTracks(
    tracks: List<TrackInfo>,
    currentTrack: TrackInfo?,
    onPlay: (TrackInfo) -> Unit,
    onStop: () -> Unit
) {
    var isFiltered by remember { mutableStateOf(false) }
    var displayedTracks by remember { mutableStateOf(tracks) }

    Text(
        text = stringResource(id = R.string.filter),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                displayedTracks = if (isFiltered) {
                    tracks
                } else {
                    tracks.sortedByDescending { it.popularity }
                }
                isFiltered = !isFiltered
            }
            .padding(end = 16.dp, top = 12.dp, bottom = 12.dp),
        color = if (isFiltered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.End
    )

    displayedTracks.forEachIndexed { index, track ->
        val isPlaying = currentTrack == track
        TrackItem(track = track, index = index, isPlaying = isPlaying) {
            if (isPlaying) {
                onStop.invoke()
            } else {
                onPlay.invoke(track)
            }
        }
    }
}