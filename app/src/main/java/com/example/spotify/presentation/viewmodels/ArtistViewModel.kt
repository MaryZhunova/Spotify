package com.example.spotify.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.data.TopTrackInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными о топ треках.
 *
 * @constructor
 * @param infoRepository репозиторий для получения информации о треках из Spotify
 */
@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val infoRepository: SpotifyInfoRepository
) : ViewModel() {

    private val _topTracks = mutableStateOf<List<TopTrackInfo>>(emptyList())

    private val _artist = mutableStateOf<ArtistInfo?>(null)

    private val _isLoading = mutableStateOf(false)

    private val _isFavoriteHighlighted = mutableStateOf(false)

    /**
     * Список треков
     */
    val topTracks: State<List<TopTrackInfo>>
        get() = _topTracks

    val artist: State<ArtistInfo?>
        get() = _artist

    /**
     * Состояние загрузки данных
     */
    val isLoading: State<Boolean>
        get() = _isLoading

    val isFavoriteHighlighted: State<Boolean>
        get() = _isFavoriteHighlighted


    /**
     * Загружает данные о топ треках
     */
    fun fetchTracksAndArtist(id: String) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        _isLoading.value = true
        _topTracks.value = infoRepository.getArtistsTopTracks(id)
        _artist.value = infoRepository.getArtistsInfo(id)
        _isLoading.value = false
    }

    fun changeIsHighlightedState() {
        _isFavoriteHighlighted.value = !_isFavoriteHighlighted.value
    }
}