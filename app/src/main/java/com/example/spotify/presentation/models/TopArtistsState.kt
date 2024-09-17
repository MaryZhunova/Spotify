package com.example.spotify.presentation.models

import com.example.spotify.domain.models.ArtistInfo

/**
 * Состояния экрана топа исполнителей пользователя
 */
sealed interface TopArtistsState {

    /**
     * Начальное состояние
     */
    data object Idle : TopArtistsState

    /**
     * Состояние, когда идет загрузка исполнителей
     */
    data object Loading : TopArtistsState

    /**
     * Состояние, когда произошла ошибка во время загрузки
     *
     * @property error тип ошибки аутентификации
     */
    data class Fail(val error: Throwable) : TopArtistsState

    /**
     * Состояние, когда загрузка прошла успешно
     *
     * @property topArtists список исполнителей
     */
    data class Success(val topArtists: List<ArtistInfo>) : TopArtistsState
}