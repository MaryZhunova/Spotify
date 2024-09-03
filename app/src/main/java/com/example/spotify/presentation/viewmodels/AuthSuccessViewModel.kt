package com.example.spotify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.models.data.UserProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val statsRepository: SpotifyStatsRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileInfo?>(null)
    val userProfile: StateFlow<UserProfileInfo?> = _userProfile

    private val _showExitDialog = MutableStateFlow(false)
    val showExitDialog: StateFlow<Boolean> = _showExitDialog

    /**
     * Загружает профиль пользователя
     */
    fun loadUserProfile() = viewModelScope.launch {
        statsRepository.getCurrentUserProfile()?.let {
            _userProfile.value = it
        }
    }

    /**
     * Изменяет статус показа диалога
     *
     * @param boolean следует ли показывать диалог (true) или нет (false)
     */
    fun changeShowDialogStatus(boolean: Boolean) {
        _showExitDialog.value = boolean
    }
}