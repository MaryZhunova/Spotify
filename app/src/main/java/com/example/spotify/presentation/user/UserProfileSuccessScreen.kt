package com.example.spotify.presentation.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotify.R
import com.example.spotify.domain.models.UserProfileInfo
import com.example.spotify.presentation.TOP_ARTISTS_SCREEN
import com.example.spotify.presentation.TOP_TRACKS_SCREEN
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.components.ParallaxUserImage
import com.example.spotify.presentation.components.UserImage

/**
 * Компонент, отображающий успешный экран профиля пользователя
 *
 * @param info Информация о пользователе, включающая изображение и отображаемое имя
 * @param onBackClick Действие, выполняемое при нажатии на кнопку "Назад".
 * @param onTopClick Действие, выполняемое при нажатии на кнопки для перехода на экраны с топ-треками или топ-артистами
 */
@Composable
fun UserProfileSuccessScreen(
    info: UserProfileInfo, onBackClick: () -> Unit, onTopClick: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    AppBar { onBackClick.invoke() }
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        ParallaxUserImage(image = info.image, name = info.displayName)
        Text(
            modifier = Modifier.padding(top = 40.dp),
            fontSize = 46.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = info.displayName
        )
    } else {
        UserImage(
            modifier = Modifier.offset(y = (-40).dp), image = info.image, name = info.displayName
        )
        Text(
            fontSize = 46.sp,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = info.displayName
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = { onTopClick.invoke(TOP_TRACKS_SCREEN) }) {
            Text(text = stringResource(id = R.string.top_tracks))
        }

        Button(onClick = { onTopClick.invoke(TOP_ARTISTS_SCREEN) }) {
            Text(text = stringResource(id = R.string.top_artists))
        }
    }
}