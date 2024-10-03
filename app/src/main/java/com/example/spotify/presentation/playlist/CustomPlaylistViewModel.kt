package com.example.spotify.presentation.playlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.models.CustomPlaylistScreenState
import com.example.spotify.presentation.models.GenreModel
import com.example.spotify.presentation.models.Model
import com.example.spotify.presentation.models.Search
import com.example.spotify.presentation.models.Selected
import com.example.spotify.presentation.models.SelectionButton
import com.example.spotify.presentation.models.TimePeriods
import com.example.spotify.utils.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomPlaylistViewModel @Inject constructor(
    private val interactor: SpotifyInteractor,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {
    private val _state = mutableStateOf<CustomPlaylistScreenState>(CustomPlaylistScreenState.Idle)
    val state: State<CustomPlaylistScreenState> get() = _state

    private val _selectionOption = mutableStateOf<SelectionOption?>(null)
    private val selectionOption: State<SelectionOption?> get() = _selectionOption

    private val _selectedGenres = mutableStateOf<List<String>>(emptyList())
    private val selectedGenres: State<List<String>> get() = _selectedGenres

    private val _selectedArtists = mutableStateOf<List<ArtistInfo>>(emptyList())
    private val selectedArtists: State<List<ArtistInfo>> get() = _selectedArtists

    private val _selectedTracks = mutableStateOf<List<TrackInfo>>(emptyList())
    private val selectedTracks: State<List<TrackInfo>> get() = _selectedTracks

    private val currentTrack: StateFlow<TrackInfo?> get() = audioPlayerManager.currentTrack

    private val _artistSearchResults = mutableStateOf<List<ArtistInfo>?>(null)
    private val artistSearchResults: State<List<ArtistInfo>?> get() = _artistSearchResults

    private val _trackSearchResults = mutableStateOf<List<TrackInfo>?>(null)
    private val trackSearchResults: State<List<TrackInfo>?> get() = _trackSearchResults

    private val _artistQuery = mutableStateOf("")
    private val artistQuery: State<String> get() = _artistQuery

    private val _trackQuery = mutableStateOf("")
    private val trackQuery: State<String> get() = _trackQuery

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }

    fun init() = viewModelScope.launch {
        _state.value = CustomPlaylistScreenState.Loading

        _state.value = CustomPlaylistScreenState.Success(
            genres = getGenreModel(),
            tracks = getTracksModel(),
            artists = getArtistsModel(),
            selectionButton = getSelectionButton()
        )
    }

    fun createPlaylist() = viewModelScope.launch {
        _state.value = CustomPlaylistScreenState.Loading
        _state.value = CustomPlaylistScreenState.PlaylistCreated(
            playlist = interactor.createPlaylist(
                genres = selectedGenres.value,
                artists = selectedArtists.value,
                tracks = selectedTracks.value
            ),
            currentTrack = currentTrack,
            onPlay = { track -> play(track) },
            onStop = { stop() }
        )
    }

    private fun getSelectionButton(): SelectionButton =
        SelectionButton(
            type = selectionOption,
            onChange = {
                _selectionOption.value = it
                when (it) {
                    SelectionOption.GENRE -> {
                        clearArtistSearchHistory()
                        clearTrackSearchHistory()
                    }

                    SelectionOption.TRACK -> {
                        clearArtistSearchHistory()
                    }

                    SelectionOption.ARTIST -> {
                        clearTrackSearchHistory()
                    }
                }
            }
        )

    private suspend fun getArtistsModel(): Model<ArtistInfo> {
        val search = Search(
            query = artistQuery,
            searchResults = artistSearchResults,
            placeholder = runCatching {
                interactor.getTopArtists(TimePeriods.LONG.strValue).take(10)
            }.getOrDefault(emptyList()),
            onSearch = { searchArtists(it) },
        )
        val selected = Selected(
            list = selectedArtists,
            onSelectedChanged = { toggleArtistSelection(it) })

        return Model(
            search = search,
            selected = selected
        )
    }

    private suspend fun getTracksModel(): Model<TrackInfo> {
        val search = Search(
            query = trackQuery,
            searchResults = trackSearchResults,
            placeholder = runCatching {
                interactor.getTopTracks(TimePeriods.LONG.strValue).take(10)
            }.getOrDefault(emptyList()),
            onSearch = { searchTracks(it) },
        )
        val selected = Selected(
            list = selectedTracks,
            onSelectedChanged = { toggleTrackSelection(it) })

        return Model(
            search = search,
            selected = selected
        )
    }

    private suspend fun getGenreModel(): GenreModel {
        val allGenres = interactor.searchGenres()
        val topGenres = interactor.getTopGenres(TimePeriods.LONG.strValue)
            .map { it.genre }
            .filter { allGenres.contains(it) }
        val filteredAllGenres = allGenres.filterNot { it in topGenres }

        return GenreModel(
            top = topGenres,
            rest = filteredAllGenres,
            selected = Selected(
                list = selectedGenres,
                onSelectedChanged = { toggleGenreSelection(it) })
        )
    }

    private fun toggleGenreSelection(genre: String) {
        _selectedGenres.value = if (_selectedGenres.value.contains(genre)) {
            _selectedGenres.value - genre
        } else {
            _selectedGenres.value + genre
        }
    }

    private fun toggleArtistSelection(artist: ArtistInfo) {
        val artistInSelected = _selectedArtists.value.find { it.id == artist.id }
        _selectedArtists.value = if (artistInSelected != null) {
            _selectedArtists.value - artistInSelected
        } else {
            _selectedArtists.value + artist
        }
    }

    private fun toggleTrackSelection(track: TrackInfo) {
        val trackInSelected = _selectedTracks.value.find { it.id == track.id }
        _selectedTracks.value = if (trackInSelected != null) {
            _selectedTracks.value - trackInSelected
        } else {
            _selectedTracks.value + track
        }
    }

    private fun searchArtists(query: String) = viewModelScope.launch {
        if (query.isNotBlank()) {
            _artistQuery.value = query
            _artistSearchResults.value =
                runCatching { interactor.searchArtists(query) }.getOrDefault(emptyList())
        } else {
            clearArtistSearchHistory()
        }
    }

    private fun searchTracks(query: String) = viewModelScope.launch {
        if (query.isNotBlank()) {
            _trackQuery.value = query
            _trackSearchResults.value =
                runCatching { interactor.searchTracks(query) }.getOrDefault(emptyList())
        } else {
            clearTrackSearchHistory()
        }
    }

    private fun clearArtistSearchHistory() {
        _artistQuery.value = ""
        _artistSearchResults.value = null
    }

    private fun clearTrackSearchHistory() {
        _artistQuery.value = ""
        _trackSearchResults.value = null
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
