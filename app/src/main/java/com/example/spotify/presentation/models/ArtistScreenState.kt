package com.example.spotify.presentation.models

/**
 * Состояния экрана исполнителя
 */
sealed interface ArtistScreenState {

    /**
     * Начальное состояние
     */
    data object Idle : ArtistScreenState

    /**
     * Состояние, когда идет загрузка исполнителей
     */
    data object Loading : ArtistScreenState

    /**
     * Состояние, когда загрузка прошла успешно
     */
    data object Success : ArtistScreenState

    /**
     * Состояние, когда произошла ошибка во время загрузки
     *
     * @property error тип ошибки аутентификации
     */
    data class Fail(val error: Throwable) : ArtistScreenState
}