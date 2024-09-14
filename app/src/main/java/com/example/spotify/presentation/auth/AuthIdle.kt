package com.example.spotify.presentation.auth

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spotify.R

/**
 * Стартовый экран
 *
 * @param onClick обработка нажатия
 */
@Composable
fun AuthIdle(onClick:() -> Unit) {
    val transition = rememberInfiniteTransition(label = "transitionLabel")
    val primaryColor by transition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "primaryColorLabel"
    )
    val colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        primaryColor,
        MaterialTheme.colorScheme.background
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(brush = Brush.verticalGradient(colors = colors))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.music_icon),
            contentDescription = stringResource(id = R.string.music_icon),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 40.dp, bottom = 30.dp)
        )
        Button(onClick = onClick) {
            Text(
                text = stringResource(id = R.string.log_in),
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}