package com.example.spotify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.models.data.net.UserProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val statsApiMapper: SpotifyStatsApiMapper
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile

    fun loadUserProfile(accessToken: String) {
        viewModelScope.launch {
            statsApiMapper.getCurrentUserProfile(accessToken) { response ->
                response?.let {
                    _userProfile.value = it
                }
            }
        }
    }
}