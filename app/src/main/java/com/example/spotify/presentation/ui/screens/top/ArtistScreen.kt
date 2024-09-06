package com.example.spotify.presentation.ui.screens.top

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.presentation.ui.components.AppBar
import com.example.spotify.presentation.ui.components.ProgressIndicator
import com.example.spotify.presentation.viewmodels.ArtistViewModel

@Composable
fun ArtistScreen(
    navController: NavController,
    id: String
) {
    val viewModel: ArtistViewModel = hiltViewModel()

    val topTracks by viewModel.topTracks
    val isLoading by viewModel.isLoading

    LaunchedEffect(id) {
        viewModel.fetchTracks(id)
    }

    Scaffold(
        topBar = { AppBar { navController.popBackStack() } },
    ) {
        if (isLoading) {
            ProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp),
            ) {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(artistInfo.image)
//                        .crossfade(true)
//                        .build(),
//                    placeholder = painterResource(R.drawable.music_icon),
//                    error = painterResource(R.drawable.music_icon),
//                    alignment = Alignment.CenterStart,
//                    contentDescription = null,
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(1f)
//                        .clip(RoundedCornerShape(16.dp))
//                        .padding(bottom = 16.dp)
//                )
//                Text(
//                    fontSize = 36.sp,
//                    style = MaterialTheme.typography.headlineLarge,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    text = artistInfo.name
//                )
//                Text(
//                    fontSize = 26.sp,
//                    style = MaterialTheme.typography.displayLarge,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    text = artistInfo.genres
//                )
                topTracks.forEach { track ->
                    TrackItem(track = track)
                }
            }
        }
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