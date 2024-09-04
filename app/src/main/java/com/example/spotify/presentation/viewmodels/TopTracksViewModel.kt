package com.example.spotify.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.models.data.TrackInfo
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
    private val statsRepository: SpotifyStatsRepository
) : ViewModel() {

    private val _topTracks = mutableStateOf<List<TrackInfo>>(emptyList())

    /**
     * Список треков
     */
    val topTracks: State<List<TrackInfo>>
        get() = _topTracks

    private val _isLoading = mutableStateOf(false)

    /**
     * Состояние загрузки данных
     */
    val isLoading: State<Boolean>
        get() = _isLoading

    private var nextPageUrl: String? = null

    /**
     * Загружает данные о топ треках
     */
    fun fetchTopTracks() = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        _isLoading.value = true
        val info = statsRepository.getTopTracks("short_term", 50)
        _isLoading.value = false
        _topTracks.value = info.items
        nextPageUrl = info.next
    }

    /**
     * Загружает следующую страницу данных о топ треках
     */
    fun fetchNextPage() = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        nextPageUrl?.let {
            val info = statsRepository.getTopTracksNextPage(it)
            _topTracks.value = (_topTracks.value) + info.items
            nextPageUrl = info.next
        }
    }
}