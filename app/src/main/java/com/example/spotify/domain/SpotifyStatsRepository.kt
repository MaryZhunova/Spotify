package com.example.spotify.domain

import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo

interface SpotifyStatsRepository {

    fun getCurrentUserProfile(callback: (UserProfileInfo?) -> Unit)

    suspend fun getTopTracks(
        timeRange: String,
        limit: Int,
        callback: (TopTracksInfo?) -> Unit
    )

    fun getNextPage(url: String, callback: (TopTracksInfo?) -> Unit)
}