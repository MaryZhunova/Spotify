package com.example.spotify.presentation.top

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.models.TopTracksState
import com.example.spotify.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными о топ треках.
 *
 * @constructor
 * @param statsRepository репозиторий для получения информации о треках из Spotify
 * @param audioPlayerManager управляет воспроизведением треков
 */
@HiltViewModel
class TopTracksViewModel @Inject constructor(
    private val statsRepository: SpotifyUserStatsRepository,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    private val _topTracksState = mutableStateOf<TopTracksState>(TopTracksState.Idle)

    private val trackInfoItems = mutableMapOf<TimePeriods, List<TrackInfo>>()

    private val _selectedPeriod = mutableStateOf(TimePeriods.SHORT)

    /**
     * Выбранный период
     */
    val selectedPeriod: State<TimePeriods>
        get() = _selectedPeriod

    /**
     * Состояние загрузки
     */
    val topTracksState: State<TopTracksState>
        get() = _topTracksState

    /**
     * Воспроизводимый трек
     */
    val currentTrack: StateFlow<TrackInfo?>
        get() = audioPlayerManager.currentTrack

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }

    /**
     * Загружает данные о топ исполнителях
     */
    fun fetchTopTracks(period: TimePeriods) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            _topTracksState.value = TopTracksState.Fail(err)
        }
    ) {
        val savedInfo = trackInfoItems[period]
        if (savedInfo != null) {
            _topTracksState.value = TopTracksState.Success(savedInfo)
        } else {
            _topTracksState.value = TopTracksState.Loading
            val info = statsRepository.getTopTracks(timeRange = period.strValue)
            trackInfoItems.putIfAbsent(period, info)
            _topTracksState.value = TopTracksState.Success(info)
        }
    }

    /**
     * Меняет текущий временной период
     */
    fun switchSelected(period: TimePeriods) {
        _selectedPeriod.value = period
    }

    /**
     * Начать воспроизведение
     *
     * @param track трек, который нужно воспроизвести
     */
    fun play(track: TrackInfo) = audioPlayerManager.play(track)

    /**
     * Остановить воспроизведение
     */
    fun stop() = audioPlayerManager.stop()
}