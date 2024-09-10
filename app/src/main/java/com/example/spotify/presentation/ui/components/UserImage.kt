package com.example.spotify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Компонент для отображения изображения пользователя или инициалов
 *
 * @param modifier Модификатор, позволяющий изменять внешний вид или поведение компонента
 * @param image URL изображения пользователя. Если значение равно `null`, отображаются инициалы
 * @param name Имя пользователя, используемое для отображения инициалов, если изображение не предоставлено
 */
@Composable
fun UserImage(modifier: Modifier = Modifier, image: String?, name: String) {
    Box(
        modifier = modifier
            .size(82.dp)
            .border(4.dp, MaterialTheme.colorScheme.background, shape = CircleShape)
            .padding(1.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } ?: Text(
            fontSize = 46.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = name.substring(0, 1)
        )
    }
}