package com.example.spotify.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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

    // State to manage the expansion of details
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(animatedColor)
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.animateContentSize()
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
                    modifier = Modifier
                        .size(45.dp)
                        .padding(end = 8.dp)
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

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = ExitTransition.None
            ) {
                if (track.audioFeatures != null) {
                    val context = LocalContext.current
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Feature("Danceability", track.audioFeatures.danceability)
                        Feature("Energy", track.audioFeatures.energy)
                        Feature("Valence", track.audioFeatures.valence)

                        Text(
                            text = "Open on Spotify",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(track.audioFeatures.uri)
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Feature(
    name: String,
    value: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .padding(start = 8.dp)
        ) {
            val color = MaterialTheme.colorScheme.primary
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2),
                    end = Offset((value.toFloat() / 1) * size.width, size.height / 2),
                    strokeWidth = 15f
                )
            }
        }
    }
}

@Composable
private fun PlayButton(
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