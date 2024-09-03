package com.example.spotify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления таймером сессии
 */
class SessionTimerViewModel @Inject constructor(): ViewModel() {

    private var isTimerActive = false

    /**
     * Запускает таймер на 10 минут. При истечении таймера выполняется переданное действие.
     *
     * @param onTimerExpired лямбда-функция, которая будет выполнена, когда таймер истечет.
     * Если таймер уже активен, функция не будет запущена повторно.
     */
    fun startTimer(onTimerExpired: () -> Unit) {
        if (isTimerActive) return

        isTimerActive = true
        viewModelScope.launch {
            delay(10 * 60 * 1000L)
            onTimerExpired()
            isTimerActive = false
        }
    }

    /**
     * Сбрасывает таймер
     */
    fun resetTimer() {
        isTimerActive = false
    }
}