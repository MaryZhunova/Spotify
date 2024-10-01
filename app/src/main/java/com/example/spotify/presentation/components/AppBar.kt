package com.example.spotify.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.spotify.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * AppBar
 *
 * @param title заголовок экрана
 * @param onClick обработка нажатия на иконку навигации
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = "",
    bgColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit
) {
    var isButtonEnabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (isButtonEnabled) {
                        onClick.invoke()
                        isButtonEnabled = false
                        scope.launch {
                            // Throttling
                            delay(1000)
                            isButtonEnabled = true
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = stringResource(id = R.string.arrow_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = bgColor
        )
    )
}