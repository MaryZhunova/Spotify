package com.example.spotify.data.net

import com.example.spotify.models.data.net.UserProfileResponse

interface SpotifyStatsApiMapper {

    fun getCurrentUserProfile(accessToken: String, callback: (UserProfileResponse?) -> Unit)
}