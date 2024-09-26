package com.example.spotify.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import com.example.spotify.domain.models.TrackInfo

/**
 * Отображает элемент трека в списке.
 *
 * @param track Информация о треке, который будет отображен.
 * @param index Индекс трека в списке.
 * @param isHighlighted Подсвечен ли цветом.
 * @param isPlaying Флаг, указывающий, воспроизводится ли трек в данный момент.
 * @param onClick Лямбда-функция, которая будет вызвана при нажатии на элемент.
 */
@Composable
fun TrackItem(
    track: TrackInfo,
    index: Int = -1,
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(animatedColor)
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (index > -1) {
                Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(30.dp)
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(track.album.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.music_icon),
                error = painterResource(R.drawable.music_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(45.dp).padding(end = 8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = track.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = track.artists,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Popularity: ${track.popularity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
}

@Composable
fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = if (isPlaying) {
                painterResource(R.drawable.pause)
            } else {
                painterResource(R.drawable.play)
            },
            contentDescription = null
        )
    }
}