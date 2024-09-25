package com.example.spotify.presentation.artist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyInfoRepository
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления данными об исполнителе
 *
 * @constructor
 * @param infoRepository репозиторий для получения информации о треках и исполнителях из Spotify
 * @param userRepository репозиторий для получения информации о пользователе и его статистике
 * @param audioPlayerManager управляет воспроизведением треков
 */
@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val infoRepository: SpotifyInfoRepository,
    private val userRepository: SpotifyUserStatsRepository,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {
    private val _topTracks = mutableStateOf<List<TrackInfo>>(emptyList())

    private val _favoriteTracks = mutableStateOf<List<TrackInfo>>(emptyList())

    private val _artist = mutableStateOf<ArtistInfo?>(null)

    private val _isLoading = mutableStateOf(false)

    private val _isFavoriteHighlighted = mutableStateOf(false)

    /**
     * Список популярный треков исполнителя
     */
    val topTracks: State<List<TrackInfo>>
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
     * Воспроизводимый трек
     */
    val currentTrack: StateFlow<TrackInfo?>
        get() = audioPlayerManager.currentTrack

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }

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
        _favoriteTracks.value = userRepository.getTopTracksByArtistId(id)
        _isLoading.value = false
    }

    /**
     * Меняет статус выделения любимых треков пользователя среди популярных на противоположные
     */
    fun changeIsHighlightedState(): Boolean {
        val newState = !_isFavoriteHighlighted.value
        return if (newState && _topTracks.value.none { it.isFavorite }) {
            false
        } else {
            _isFavoriteHighlighted.value = newState
            true
        }
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