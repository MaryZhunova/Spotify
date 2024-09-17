package com.example.spotify.presentation.models

import com.example.spotify.domain.models.TrackInfo

/**
 * Состояния экрана топа треков пользователя
 */
sealed interface TopTracksState {

    /**
     * Начальное состояние
     */
    data object Idle : TopTracksState

    /**
     * Состояние, когда идет загрузка треков
     */
    data object Loading : TopTracksState

    /**
     * Состояние, когда произошла ошибка во время загрузки
     *
     * @property error тип ошибки аутентификации
     */
    data class Fail(val error: Throwable) : TopTracksState

    /**
     * Состояние, когда загрузка прошла успешно
     *
     * @property topTracks список треков
     */
    data class Success(val topTracks: List<TrackInfo>) : TopTracksState
}