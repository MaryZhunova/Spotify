package com.example.spotify.models.presentation

import com.example.spotify.models.data.UserProfileInfo

/**
 * Интерфейс, представляющий различные состояния профиля пользователя.
 *
 * Этот интерфейс используется для обозначения различных состояний, которые может принимать профайл пользователя,
 * такие как состояние ожидания, успешное получение данных или ошибка.
 */
sealed interface UserProfileState {

    /**
     * Начальное состояние
     */
    data object Idle : UserProfileState

    /**
     * Состояние, когда произошла ошибка при получении данных профиля пользователя.
     *
     * @property err Тип ошибки, которая произошла
     */
    data class Error(val err: Throwable = Exception()) : UserProfileState

    /**
     * Состояние, когда аутентификация прошла успешно и данные профиля пользователя успешно получены.
     *
     * @property info Информация о профиле пользователя, включающая изображение и имя.
     */
    data class Success(val info: UserProfileInfo) : UserProfileState
}
