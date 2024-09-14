package com.example.spotify.presentation.top

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.presentation.models.TimePeriods
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

    private val artistsInfoItems = mutableMapOf<TimePeriods, List<ArtistInfo>>()

    private val _selectedPeriod = mutableStateOf(TimePeriods.SHORT)

    private val _topArtists = mutableStateOf<List<ArtistInfo>>(emptyList())

    private val _isLoading = mutableStateOf(false)

    /**
     * Выбранный период
     */
    val selectedPeriod: State<TimePeriods>
        get() = _selectedPeriod

    /**
     * Список исполнителей
     */
    val topArtists: State<List<ArtistInfo>>
        get() = _topArtists

    /**
     * Состояние загрузки данных
     */
    val isLoading: State<Boolean>
        get() = _isLoading

    /**
     * Загружает данные о топ исполнителях
     */
    fun fetchTopArtists(period: TimePeriods) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        val savedInfo = artistsInfoItems[period]
        if (savedInfo != null) {
            _topArtists.value = savedInfo
        } else {
            _isLoading.value = true
            val info = statsRepository.getTopArtists(period.strValue)
            artistsInfoItems.putIfAbsent(period, info)
            _topArtists.value = info
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
