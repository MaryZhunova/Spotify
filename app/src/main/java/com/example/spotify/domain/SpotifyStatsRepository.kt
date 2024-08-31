package com.example.spotify.domain

import com.example.spotify.models.data.UserProfileInfo

interface SpotifyStatsRepository {

    fun getCurrentUserProfile(accessToken: String, callback: (UserProfileInfo?) -> Unit)
}