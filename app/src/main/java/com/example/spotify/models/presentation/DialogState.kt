package com.example.spotify.models.presentation

sealed interface DialogState {

    data object Idle: DialogState

    data class Simple(
        val title: String,
        val text: String? = null,
        val onDismiss:  () -> Unit,
        val onNegative: Button? = null,
        val onPositive: Button,
        val dismissOnClickOutside: Boolean = true
    ): DialogState
}

data class Button(
    val title: String,
    val action: () -> Unit
)