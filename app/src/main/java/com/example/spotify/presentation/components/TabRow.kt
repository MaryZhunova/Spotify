package com.example.spotify.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spotify.presentation.models.TimePeriods

/**
 * Компонент для отображения строки вкладок.
 *
 * @param selectedPeriod Текущий выбранный период времени.
 * @param periods Список доступных периодов времени.
 * @param onSwitch Функция, вызываемая при переключении вкладки, принимает выбранный период времени.
 */
@Composable
fun TabRow(
    selectedPeriod: TimePeriods,
    periods: List<TimePeriods>,
    onSwitch: (TimePeriods) -> Unit
) {
    TabRow(
        selectedTabIndex = periods.indexOf(selectedPeriod),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        periods.forEach { period ->
            Tab(
                selected = selectedPeriod == period,
                onClick = { onSwitch.invoke(period) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 6.dp),
                    text = period.nameValue,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}