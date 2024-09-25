package com.example.spotify.presentation.models

/**
 * Состояние аутентификации в приложении
 */
sealed interface AuthState {

    /**
     * Состояние, когда аутентификация не начата и ожидает действий пользователя
     */
    data object Idle : AuthState

    /**
     * Cостояние проверки актуальности кода доступа пользователя
     */
    data object Loading : AuthState

    /**
     * Состояние, когда произошла ошибка аутентификации
     *
     * @property error тип ошибки аутентификации
     */
    data class Fail(val error: AuthError) : AuthState

    /**
     * Состояние, когда аутентификация прошла успешно
     */
    data object Success : AuthState
}

/**
 * Перечисление возможных ошибок аутентификации
 */
enum class AuthError {

    /**
     * Ошибка, указывающая на отсутствие приложения Spotify на устройстве
     */
    NO_SPOTIFY,

    /**
     * Ошибка, указывающая на неудачную попытку аутентификации
     */
    AUTH_FAIL
}