package com.example.spotify.data

import com.example.spotify.data.converter.TopTracksResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.data.security.TokenStorage
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo
import javax.inject.Inject

class SpotifyStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyStatsApiMapper,
    private val userProfileConverter: UserProfileResponseToInfoConverter,
    private val topTracksConverter: TopTracksResponseToInfoConverter,
    private val storage: TokenStorage
) : SpotifyStatsRepository {

    override fun getCurrentUserProfile(accessToken: String, callback: (UserProfileInfo?) -> Unit) {
        apiMapper.getCurrentUserProfile(accessToken) { response ->
            response?.let {
                callback(userProfileConverter.convert(it))
            } ?: callback(null)
        }
    }

    override suspend fun getTopTracks(
        timeRange: String,
        limit: Int,
        callback: (TopTracksInfo?) -> Unit
    ) {
        storage.getAccessToken()?.let {
            apiMapper.getTopTracks(it, timeRange, limit) { response ->
                response?.let { tracks ->
                    callback(topTracksConverter.convert(tracks))
                } ?: callback(null)
            }
        }
    }

    override fun getNextPage(url: String, callback: (TopTracksInfo?) -> Unit) {
        apiMapper.getNextPage(url) { response ->
            response?.let {
                callback(topTracksConverter.convert(it))
            } ?: callback(null)
        }
    }
}