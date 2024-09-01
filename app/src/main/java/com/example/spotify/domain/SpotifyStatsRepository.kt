package com.example.spotify.domain

import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo

interface SpotifyStatsRepository {

    suspend fun getCurrentUserProfile(): UserProfileInfo?

    suspend fun getTopTracks(
        timeRange: String,
        limit: Int
    ): TopTracksInfo?

    suspend fun getNextPage(url: String): TopTracksInfo?
}