package com.example.spotify.presentation.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.spotify.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Компонент для отображения изображения пользователя с эффектом параллакса на фоне
 *
 * @param image URL изображения пользователя
 * @param name имя пользователя, которое отображается, если изображение отсутствует
 */
@Composable
fun ParallaxUserImage(image: String?, name: String) {
    val context = LocalContext.current
    val sensorManager =
        remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    var xOffsetFront by remember { mutableFloatStateOf(0f) }
    var xOffsetBack by remember { mutableFloatStateOf(0f) }

    val maxOffsetPx = with(LocalDensity.current) { 50.dp.toPx() }

    val coroutineScope = rememberCoroutineScope()

    val gyroscopeListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    coroutineScope.launch(Dispatchers.Main) {
                        xOffsetFront = (xOffsetFront - event.values[1] * ScaleFactorFront)
                            .coerceIn(-maxOffsetPx, maxOffsetPx)
                        xOffsetBack = (xOffsetBack - event.values[1] * ScaleFactorBack)
                            .coerceIn(-maxOffsetPx, maxOffsetPx)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(
            gyroscopeListener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_UI
        )
        onDispose {
            sensorManager.unregisterListener(gyroscopeListener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(0.5f))
    ) {
        Image(
            painter = painterResource(id = R.drawable.wave_1),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(0.3f)),
            contentDescription = stringResource(id = R.string.bg_image),
            modifier = Modifier
                .wrapContentWidth(unbounded = true)
                .offset {
                    IntOffset(xOffsetBack.roundToInt(), 0)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.wave),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(0.5f)),
            contentDescription = stringResource(id = R.string.bg_image),
            contentScale = ContentScale.None,
            modifier = Modifier
                .wrapContentWidth(unbounded = true)
                .offset {
                    IntOffset(xOffsetFront.roundToInt(), 0)
                }
        )
        UserImage(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 40.dp),
            image = image,
            name = name
        )
    }
}


private const val ScaleFactorFront = 6f
private const val ScaleFactorBack = 3f