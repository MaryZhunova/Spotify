package com.example.spotify.models.presentation

sealed interface AuthState {

    data object Idle: AuthState

    data class Fail(val error: AuthError): AuthState

    data class Success(val accessToken: String): AuthState
}

enum class AuthError {
    NO_SPOTIFY,
    AUTH_FAIL
}