package com.example.spotify.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.models.presentation.Button
import com.example.spotify.models.presentation.DialogState
import com.example.spotify.models.presentation.UserProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для управления состоянием экрана успешной аутентификации
 *
 * @constructor
 * @param statsRepository репозиторий для получения информации
 * о пользователе и статистики по трекам и исполнителях из Spotify
 */
@HiltViewModel
class AuthSuccessViewModel @Inject constructor(
    private val statsRepository: SpotifyUserStatsRepository
) : ViewModel() {

    private val _userProfile = mutableStateOf<UserProfileState>(UserProfileState.Idle)

    /**
     * Данные пользователя
     */
    val userProfile: State<UserProfileState> = _userProfile

    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Idle)

    /**
     * Состояние отображения алерта
     */
    val dialogState: StateFlow<DialogState> = _dialogState

    /**
     * Загружает профиль пользователя
     */
    suspend fun loadUserProfile() = viewModelScope.launch(
        CoroutineExceptionHandler { _, err ->
           _userProfile.value = UserProfileState.Error(err)
        }
    ) {
        val userInfo = statsRepository.getCurrentUserProfile()
        _userProfile.value = UserProfileState.Success(userInfo)
    }

    /**
     * Показывает алерт выхода из приложения
     *
     * @param callback обработка действия при подтверждении выхода
     */
    fun showExitDialog(callback: () -> Unit) {
        _dialogState.value = DialogState.Simple(
            title = "Are you sure you want to exit?",
            onPositive = Button(title = "Confirm") {
                _dialogState.value = DialogState.Idle
                callback.invoke()
            },
            onNegative = Button(title = "Dismiss") { _dialogState.value = DialogState.Idle },
            onDismiss = { _dialogState.value = DialogState.Idle }
        )
    }
}