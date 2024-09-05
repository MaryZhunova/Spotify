package com.example.spotify.models.presentation

import com.example.spotify.models.data.UserProfileInfo

/**
 *
 */
sealed interface UserProfileState {

    /**
     *
     */
    data object Idle : UserProfileState

    /**
     *
     *
     * @property err тип ошибки
     */
    data class Error(val err: Throwable = Exception()) : UserProfileState

    /**
     * Состояние, когда аутентификация прошла успешно
     */
    data class Success(val info: UserProfileInfo) : UserProfileState
}