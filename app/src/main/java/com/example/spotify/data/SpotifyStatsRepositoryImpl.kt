package com.example.spotify.data

import com.example.spotify.data.converter.TopTracksResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotifyStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyStatsApiMapper,
    private val userProfileConverter: UserProfileResponseToInfoConverter,
    private val topTracksConverter: TopTracksResponseToInfoConverter,
    private val securityRepository: SecurityRepository
) : SpotifyStatsRepository {

    override suspend fun getCurrentUserProfile(): UserProfileInfo? = withContext(Dispatchers.IO) {
            securityRepository.getAccessToken()?.let { token ->
                apiMapper.getCurrentUserProfile(token)?.let { userProfileConverter.convert(it) }
            }
    }

    override suspend fun getTopTracks(timeRange: String, limit: Int): TopTracksInfo? =
        withContext(Dispatchers.IO) {
                securityRepository.getAccessToken()?.let { token ->
                    apiMapper.getTopTracks(token, timeRange, limit)
                        ?.let { topTracksConverter.convert(it) }
                }
        }

    override suspend fun getNextPage(url: String): TopTracksInfo? = withContext(Dispatchers.IO) {
            securityRepository.getAccessToken()?.let { token ->
                apiMapper.getNextPage(token, url)?.let { topTracksConverter.convert(it) }
            }
    }
}