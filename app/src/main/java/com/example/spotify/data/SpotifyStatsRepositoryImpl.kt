package com.example.spotify.data

import com.example.spotify.data.converter.TopArtistsResponseToInfoConverter
import com.example.spotify.data.converter.TopTracksResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.data.TopArtistsInfo
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.UserProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotifyStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyStatsApiMapper,
    private val userProfileConverter: UserProfileResponseToInfoConverter,
    private val topTracksConverter: TopTracksResponseToInfoConverter,
    private val topArtistsConverter: TopArtistsResponseToInfoConverter,
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

    override suspend fun getTopTracksNextPage(url: String): TopTracksInfo? =
        withContext(Dispatchers.IO) {
            securityRepository.getAccessToken()?.let { token ->
                apiMapper.getTopTracksNextPage(token, url)?.let { topTracksConverter.convert(it) }
            }
        }

    override suspend fun getTopArtists(timeRange: String, limit: Int): TopArtistsInfo? =
        withContext(Dispatchers.IO) {
            securityRepository.getAccessToken()?.let { token ->
                apiMapper.getTopArtists(token, timeRange, limit)
                    ?.let { topArtistsConverter.convert(it) }
            }
        }

    override suspend fun getTopArtistsNextPage(url: String): TopArtistsInfo? =
        withContext(Dispatchers.IO) {
            securityRepository.getAccessToken()?.let { token ->
                apiMapper.getTopArtistsNextPage(token, url)?.let { topArtistsConverter.convert(it) }
            }
        }

}