package com.example.spotify.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spotify.R

/**
 * Компонент, отображающий экран с ошибкой
 *
 * Этот экран содержит фоновую заливку с радиальным градиентом и текст, указывающий на наличие ошибки
 *
 * @param errorBlock Компонент, который будет отображён внутри экрана ошибки. Может использоваться для отображения
 * дополнительной информации об ошибке
 */
@Composable
fun ErrorScreen(errorBlock: @Composable () -> Unit) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.background
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = colors,
                        center = Offset(size.width / 2, 0f),
                        radius = size.height * 0.6f
                    )
                )
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ErrorIcon()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            errorBlock.invoke()
        }
    }
}

@Composable
fun ErrorIcon() {
    Icon(
        painter = painterResource(id = R.drawable.error),
        tint = MaterialTheme.colorScheme.error,
        contentDescription = null
    )
}