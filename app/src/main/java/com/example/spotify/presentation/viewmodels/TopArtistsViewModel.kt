package com.example.spotify.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.presentation.TimePeriods
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
    private val statsRepository: SpotifyStatsRepository
) : ViewModel() {

    private val artistsInfoItems = mutableMapOf<TimePeriods, List<ArtistInfo>>()

    private val _selectedPeriod = mutableStateOf(TimePeriods.SHORT)

    val selectedPeriod: State<TimePeriods>
        get() = _selectedPeriod


    private val _topArtists = mutableStateOf<List<ArtistInfo>>(emptyList())

    /**
     * Список исполнителей
     */
    val topArtists: State<List<ArtistInfo>>
        get() = _topArtists

    private val _isLoading = mutableStateOf(false)

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
            val items = statsRepository.getTopArtists(period.strValue).items
            artistsInfoItems.putIfAbsent(period, items)
            _topArtists.value = items
            _isLoading.value = false
        }
    }

    fun switchSelected(period: TimePeriods) {
        _selectedPeriod.value = period
    }
}
