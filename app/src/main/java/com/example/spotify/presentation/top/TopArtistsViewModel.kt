package com.example.spotify.presentation.top

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.presentation.models.TopArtistsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными о топ исполнителях.
 *
 * @constructor
 * @param statsRepository репозиторий для получения информации об исполнителях из Spotify
 */
@HiltViewModel
class TopArtistsViewModel @Inject constructor(
    private val statsRepository: SpotifyUserStatsRepository
) : ViewModel() {

    private val _topArtistsState = mutableStateOf<TopArtistsState>(TopArtistsState.Idle)

    private val artistsInfoItems = mutableMapOf<TimePeriods, List<ArtistInfo>>()

    private val _selectedPeriod = mutableStateOf(TimePeriods.SHORT)

    /**
     * Выбранный период
     */
    val selectedPeriod: State<TimePeriods>
        get() = _selectedPeriod

    /**
     * Состояние загрузки
     */
    val topArtistsState: State<TopArtistsState>
        get() = _topArtistsState

    /**
     * Загружает данные о топ исполнителях
     */
    fun fetchTopArtists(period: TimePeriods) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            _topArtistsState.value = TopArtistsState.Fail(err)
        }
    ) {
        val savedInfo = artistsInfoItems[period]
        if (savedInfo != null) {
            _topArtistsState.value = TopArtistsState.Success(savedInfo)
        } else {
            _topArtistsState.value = TopArtistsState.Loading
            val info = statsRepository.getTopArtists(period.strValue)
            artistsInfoItems.putIfAbsent(period, info)
            _topArtistsState.value = TopArtistsState.Success(info)

        }
    }

    /**
     * Меняет текущий временной период
     */
    fun switchSelected(period: TimePeriods) {
        _selectedPeriod.value = period
    }
}
