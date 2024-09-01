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

@HiltViewModel
class AuthSuccessViewModel @Inject constructor(
    private val statsRepository: SpotifyStatsRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileInfo?>(null)
    val userProfile: StateFlow<UserProfileInfo?> = _userProfile

    fun loadUserProfile() = viewModelScope.launch {
        statsRepository.getCurrentUserProfile()?.let {
                _userProfile.value = it
            }
        }
}