package com.example.spotify.presentation.models

import com.example.spotify.domain.TopGenre

/**
 * Состояния экрана топа треков пользователя
 */
sealed interface TopGenresState {

    /**
     * Начальное состояние
     */
    data object Idle : TopGenresState

    /**
     * Состояние, когда идет загрузка треков
     */
    data object Loading : TopGenresState

    /**
     * Состояние, когда произошла ошибка во время загрузки
     *
     * @property error тип ошибки аутентификации
     */
    data class Fail(val error: Throwable) : TopGenresState

    /**
     * Состояние, когда загрузка прошла успешно
     *
     * @property topTracks список треков
     */
    data class Success(val topGenres: List<TopGenre>) : TopGenresState
}