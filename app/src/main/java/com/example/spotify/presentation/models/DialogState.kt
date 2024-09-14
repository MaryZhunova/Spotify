package com.example.spotify.presentation.models

/**
 * Интерфейс, представляющий различные состояния алерта
 */
sealed interface DialogState {

    /**
     * Состояние, когда алерт не отображается
     */
    data object Idle : DialogState

    /**
     * Простой алерт.
     *
     * Это состояние представляет простой алерт с заголовком, текстом и кнопками
     * Оно может содержать кнопку подтверждения и кнопку отмены (опционально)
     *
     * @property title Заголовок диалогового окна.
     * @property text Опциональный текст сообщения в диалоговом окне. Может быть равен `null`.
     * @property onDismiss Функция, которая вызывается при закрытии диалогового окна.
     * @property onNegative Опциональная кнопка отмены с текстом и действием. Может быть равна `null`.
     * @property onPositive Кнопка подтверждения с текстом и действием.
     * @property dismissOnClickOutside Флаг, указывающий, следует ли закрывать алерт при нажатии вне его границ. По умолчанию `true`.
     */
    data class Simple(
        val title: String,
        val text: String? = null,
        val onDismiss: () -> Unit,
        val onNegative: Button? = null,
        val onPositive: Button,
        val dismissOnClickOutside: Boolean = true
    ) : DialogState
}

/**
 * Класс, представляющий кнопку в алерте
 *
 * @property title Текст, отображаемый на кнопке.
 * @property action Функция, которая вызывается при нажатии на кнопку.
 */
data class Button(
    val title: String,
    val action: () -> Unit
)
