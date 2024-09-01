package com.example.spotify.data

import com.example.spotify.data.converter.TopTracksResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo
import javax.inject.Inject

class SpotifyStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyStatsApiMapper,
    private val userProfileConverter: UserProfileResponseToInfoConverter,
    private val topTracksConverter: TopTracksResponseToInfoConverter,
    private val securityRepository: SecurityRepository
) : SpotifyStatsRepository {

    override fun getCurrentUserProfile(callback: (UserProfileInfo?) -> Unit) {
        securityRepository.getAccessToken()?.let { token ->
            apiMapper.getCurrentUserProfile(token) { response ->
                response?.let {
                    callback(userProfileConverter.convert(it))
                } ?: callback(null)
            }
        }
    }

    override suspend fun getTopTracks(
        timeRange: String,
        limit: Int,
        callback: (TopTracksInfo?) -> Unit
    ) {
        securityRepository.getAccessToken()?.let { token ->
            apiMapper.getTopTracks(token, timeRange, limit) { response ->
                response?.let { tracks ->
                    callback(topTracksConverter.convert(tracks))
                } ?: callback(null)
            }
        }
    }

    override fun getNextPage(url: String, callback: (TopTracksInfo?) -> Unit) {
        securityRepository.getAccessToken()?.let { token ->
            apiMapper.getNextPage(token, url) { response ->
                response?.let {
                    callback(topTracksConverter.convert(it))
                } ?: callback(null)
            }
        }
    }
}