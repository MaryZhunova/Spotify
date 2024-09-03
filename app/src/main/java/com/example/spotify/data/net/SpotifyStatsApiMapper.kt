package com.example.spotify.data.net

import com.example.spotify.models.data.net.TopArtistsResponse
import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse

interface SpotifyStatsApiMapper {

    suspend fun getCurrentUserProfile(accessToken: String): UserProfileResponse?

    suspend fun getTopTracks(
        accessToken: String,
        timeRange: String,
        limit: Int
    ): TopTracksResponse?

    suspend fun getTopTracksNextPage(accessToken: String, url: String): TopTracksResponse?

    suspend fun getTopArtists(
        accessToken: String,
        timeRange: String,
        limit: Int
    ): TopArtistsResponse?

    suspend fun getTopArtistsNextPage(accessToken: String, url: String): TopArtistsResponse?
}