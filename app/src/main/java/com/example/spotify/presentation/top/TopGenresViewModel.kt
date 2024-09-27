package com.example.spotify.presentation.top

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.models.TopGenresState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными о топ жанрах.
 *
 * @constructor
 * @param spotifyInteractor интерактор для получения информации о треках и исполнителях
 */
@HiltViewModel
class TopGenresViewModel @Inject constructor(
    private val spotifyInteractor: SpotifyInteractor
) : ViewModel() {

    private val _topGenresState = mutableStateOf<TopGenresState>(TopGenresState.Idle)

    private val genresInfoItems = mutableMapOf<TimePeriods, List<GenreInfo>>()

    private val _selectedPeriod = mutableStateOf(TimePeriods.SHORT)

    /**
     * Выбранный период
     */
    val selectedPeriod: State<TimePeriods>
        get() = _selectedPeriod

    /**
     * Состояние загрузки
     */
    val topGenresState: State<TopGenresState>
        get() = _topGenresState

    /**
     * Загружает данные о топ жанрах
     */
    fun fetchTopGenres(period: TimePeriods) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            _topGenresState.value = TopGenresState.Fail(err)
        }
    ) {
        val savedInfo = genresInfoItems[period]
        if (savedInfo != null) {
            _topGenresState.value = TopGenresState.Success(savedInfo)
        } else {
            _topGenresState.value = TopGenresState.Loading
            val info = spotifyInteractor.getTopGenres(timeRange = period.strValue)
            genresInfoItems.putIfAbsent(period, info)
            _topGenresState.value = TopGenresState.Success(info)
        }
    }

    /**
     * Меняет текущий временной период
     */
    fun switchSelected(period: TimePeriods) {
        _selectedPeriod.value = period
    }
}