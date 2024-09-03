package com.example.spotify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SessionTimerViewModel @Inject constructor(): ViewModel() {

    private var isTimerActive = false

    fun startTimer(onTimerExpired: () -> Unit) {
        if (isTimerActive) return

        isTimerActive = true
        viewModelScope.launch {
            delay(10 * 60 * 1000L)
            onTimerExpired()
            isTimerActive = false
        }
    }

    fun resetTimer() {
        isTimerActive = false
    }
}