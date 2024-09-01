package com.example.spotify.data.net

import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse

interface SpotifyStatsApiMapper {

    fun getCurrentUserProfile(accessToken: String, callback: (UserProfileResponse?) -> Unit)

    fun getTopTracks(
        accessToken: String,
        timeRange: String,
        limit: Int,
        callback: (TopTracksResponse?) -> Unit
    )

    fun getNextPage(accessToken: String, url: String, callback: (TopTracksResponse?) -> Unit)
}