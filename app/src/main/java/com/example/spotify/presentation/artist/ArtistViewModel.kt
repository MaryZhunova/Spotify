package com.example.spotify.presentation.artist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.models.data.ArtistInfo
import com.example.spotify.models.data.TopTrackInfo
import com.example.spotify.models.data.TrackInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными об исполнителе
 *
 * @constructor
 * @param infoRepository репозиторий для получения информации о треках и исполнителях из Spotify
 * @param userRepository репозиторий для получения информации о пользователе и его статистике
 */
@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val infoRepository: SpotifyInfoRepository,
    private val userRepository: SpotifyUserStatsRepository
) : ViewModel() {

    private val _topTracks = mutableStateOf<List<TopTrackInfo>>(emptyList())

    private val _favoriteTracks = mutableStateOf<List<TrackInfo>>(emptyList())

    private val _artist = mutableStateOf<ArtistInfo?>(null)

    private val _isLoading = mutableStateOf(false)

    private val _isFavoriteHighlighted = mutableStateOf(false)

    /**
     * Список популярный треков исполнителя
     */
    val topTracks: State<List<TopTrackInfo>>
        get() = _topTracks

    /**
     * Список любимых треков пользователя
     */
    val favoriteTracks: State<List<TrackInfo>>
        get() = _favoriteTracks

    /**
     * Информация об исполнителе
     */
    val artist: State<ArtistInfo?>
        get() = _artist

    /**
     * Состояние загрузки данных
     */
    val isLoading: State<Boolean>
        get() = _isLoading

    /**
     * Выделены ли любимые треки пользователя среди популярных
     */
    val isFavoriteHighlighted: State<Boolean>
        get() = _isFavoriteHighlighted


    /**
     * Загружает данные об исполнителе и популярных треках
     *
     * @param id идентификатор исполнителя
     */
    fun fetchTracksAndArtist(id: String) = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
            //todo
        }
    ) {
        _isLoading.value = true
        _topTracks.value = infoRepository.getArtistsTopTracks(id)
        _artist.value = infoRepository.getArtistsInfo(id)
        _favoriteTracks.value = userRepository.getTopTracks(id)
        _isLoading.value = false
    }

    /**
     * Меняет статус выделения любимых треков пользователя среди популярных на противоположные
     */
    fun changeIsHighlightedState(): Boolean {
        _isFavoriteHighlighted.value = !_isFavoriteHighlighted.value
        return _isFavoriteHighlighted.value && _topTracks.value.none { it.isFavorite }
    }
}