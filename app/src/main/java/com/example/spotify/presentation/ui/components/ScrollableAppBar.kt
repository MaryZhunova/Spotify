package com.example.spotify.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.spotify.R
import kotlin.math.roundToInt

/**
 * Компонент, отображающий прокручиваемый AppBar с фоновым изображением и заголовком
 *
 * @param modifier Модификатор, позволяющий изменять внешний вид или поведение компонента
 * @param backgroundImage URL или ресурс фонового изображения, которое будет отображаться на AppBar
 * @param title Текст заголовка, который отображается в нижней части AppBar
 * @param scrollableAppBarHeight Высота AppBar в dp
 * @param toolbarOffsetHeightPx Значение смещения AppBar в пикселях, которое используется для его перемещения во время прокрутки
 * @param onClick Действие, которое выполняется при нажатии на кнопку (например, возврат к предыдущему экрану)
 */
@Composable
fun ScrollableAppBar(
    modifier: Modifier = Modifier,
    backgroundImage: String,
    title: String,
    scrollableAppBarHeight: Dp,
    toolbarOffsetHeightPx: MutableState<Float>,
    onClick: () -> Unit
) {
    val toolBarHeight = 100.dp
    val maxOffsetHeightPx = with(LocalDensity.current) {
        scrollableAppBarHeight.roundToPx().toFloat() - toolBarHeight.roundToPx().toFloat()
    }
    val titleOffsetWidthReferenceValue = with(LocalDensity.current) { 48.dp.roundToPx().toFloat() }

    Box(modifier = Modifier
        .height(scrollableAppBarHeight)
        .offset {
            IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt())
        }
        .fillMaxWidth()) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(backgroundImage)
                .crossfade(true)
                .build(),
            alignment = Alignment.TopCenter,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Black.copy(0.3f),
                        blendMode = BlendMode.SrcOver
                    )
                }
        )
        IconButton(modifier = modifier
            .padding()
            .offset {
                IntOffset(
                    x = 0,
                    y = -toolbarOffsetHeightPx.value.roundToInt()
                )
            }
            .height(toolBarHeight)
            .padding(start = 4.dp, top = 35.dp), onClick = onClick) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = stringResource(id = R.string.arrow_icon)
            )
        }

        Box(
            modifier = Modifier
                .height(toolBarHeight)
                .padding(top = 56.dp)
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .offset {
                    IntOffset(
                        x = -((toolbarOffsetHeightPx.value / maxOffsetHeightPx) * titleOffsetWidthReferenceValue).roundToInt(),
                        y = 0
                    )
                }, contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .matchParentSize(),
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}