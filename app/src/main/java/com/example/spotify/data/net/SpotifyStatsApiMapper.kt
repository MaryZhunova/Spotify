package com.example.spotify.data.net

import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse

interface SpotifyStatsApiMapper {

    suspend fun getCurrentUserProfile(accessToken: String): UserProfileResponse?

    suspend fun getTopTracks(
        accessToken: String,
        timeRange: String,
        limit: Int
    ): TopTracksResponse?

    suspend fun getNextPage(accessToken: String, url: String): TopTracksResponse?
}