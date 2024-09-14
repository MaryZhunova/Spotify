package com.example.spotify.presentation.top

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.TimePeriods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными о топ треках.
 *
 * @constructor
 * @param statsRepository репозиторий для получения информации о треках из Spotify
 */
@HiltViewModel
class TopTracksViewModel @Inject constructor(
    private val statsRepository: SpotifyUserStatsRepository
) : ViewModel() {

    private val trackInfoItems = mutableMapOf<TimePeriods, List<TrackInfo>>()

    private val _selectedPeriod = mutableStateOf(TimePeriods.SHORT)

    private val _topTracks = mutableStateOf<List<TrackInfo>>(emptyList())

    private val _isLoading = mutableStateOf(false)

    /**
     * Выбранный период
     */
    val selectedPeriod: State<TimePeriods>
        get() = _selectedPeriod

    /**
     * Список исполнителей
     */
    val topTracks: State<List<TrackInfo>>
        get() = _topTracks

    /**
     * Состояние загрузки данных
     */
    val isLoading: State<Boolean>
        get() = _isLoading

    /**
     * Загружает данные о топ исполнителях
     */
    fun fetchTopTracks(period: TimePeriods) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        val savedInfo = trackInfoItems[period]
        if (savedInfo != null) {
            _topTracks.value = savedInfo
        } else {
            _isLoading.value = true
            val info = statsRepository.getTopTracks(timeRange = period.strValue)
            trackInfoItems.putIfAbsent(period, info)
            _topTracks.value = info
            _isLoading.value = false
        }
    }

    /**
     * Меняет текущий временной период
     */
    fun switchSelected(period: TimePeriods) {
        _selectedPeriod.value = period
    }
}