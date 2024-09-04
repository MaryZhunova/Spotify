package com.example.spotify.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.models.data.ArtistInfo
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

    private var nextPageUrl: String? = null

    /**
     * Загружает данные о топ исполнителях
     */
    fun fetchTopArtists() = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        _isLoading.value = true
        val info = statsRepository.getTopArtists("short_term", 50)
        _isLoading.value = false
        _topArtists.value = info.items
        nextPageUrl = info.next
    }

    /**
     * Загружает следующую страницу данных о топ исполнителях
     */
    fun fetchNextPage() = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        nextPageUrl?.let {
            val info = statsRepository.getTopArtistsNextPage(it)
            _topArtists.value = (_topArtists.value) + info.items
            nextPageUrl = info.next
        }
    }
}