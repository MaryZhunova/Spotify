package com.example.spotify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления таймером сессии
 */
class SessionTimerViewModel @Inject constructor() : ViewModel() {

    private val _onSessionExpired = MutableSharedFlow<Unit>()

    val onSessionExpired: SharedFlow<Unit>
        get() = _onSessionExpired.asSharedFlow()

    private var timerJob: Job? = null

    /**
     * Запускает таймер на 10 минут. При истечении таймера выполняется переданное действие.
     * Если таймер уже активен, функция не будет запущена повторно.
     */
    fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = viewModelScope.launch {
            delay(10 * 60 * 1000L)
            _onSessionExpired.emit(Unit)
        }
    }

    /**
     * Сбрасывает таймер, останавливая текущий и начиная новый.
     */
    fun resetTimer() {
        timerJob?.cancel()
        startTimer()
    }
}