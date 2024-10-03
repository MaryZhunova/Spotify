package com.example.spotify.presentation.models

import androidx.compose.runtime.State
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.playlist.SelectionOption
import kotlinx.coroutines.flow.StateFlow

/**
 * Состояния экрана исполнителя
 */
sealed interface CustomPlaylistScreenState {

    /**
     * Начальное состояние
     */
    data object Idle : CustomPlaylistScreenState

    /**
     * Состояние, когда идет загрузка исполнителей
     */
    data object Loading : CustomPlaylistScreenState

    /**
     * Состояние, когда загрузка прошла успешно
     */
    data class Success(
        val genres: GenreModel,
        val tracks: Model<TrackInfo>,
        val artists: Model<ArtistInfo>,
        val selectionButton: SelectionButton
    ) : CustomPlaylistScreenState

    /**
     * Состояние, когда загрузка прошла успешно
     */
    data object PlaylistCreating : CustomPlaylistScreenState


    /**
     * Состояние, когда загрузка прошла успешно
     */
    data class PlaylistCreated(
        val playlist: List<TrackInfo>,
        val currentTrack: StateFlow<TrackInfo?>,
        val onPlay: (TrackInfo) -> Unit,
        val onStop: () -> Unit
    ): CustomPlaylistScreenState

    /**
     * Состояние, когда произошла ошибка во время загрузки
     *
     * @property error тип ошибки аутентификации
     */
    data class Fail(val error: Throwable) : CustomPlaylistScreenState
}


data class GenreModel(
    val top: List<String>,
    val rest: List<String>,
    val selected: Selected<String>
)
data class Model<T>(
    val selected: Selected<T>,
    val search: Search<T>
)


data class Selected<T>(
    val list: State<List<T>>,
    val onSelectedChanged: (T) -> Unit
)


data class SelectionButton(
    val type: State<SelectionOption?>,
    val onChange: (SelectionOption) -> Unit
)

data class Search<T>(
    val query: State<String>,
    val searchResults: State<List<T>?>,
    val placeholder: List<T>,
    val onSearch: (String) -> Unit
)