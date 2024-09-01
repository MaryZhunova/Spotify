package com.example.spotify.data.net

import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotifyStatsApiMapperImpl @Inject constructor(
    private val apiService: SpotifyStatsApiService
) : SpotifyStatsApiMapper {

    override suspend fun getCurrentUserProfile(accessToken: String): UserProfileResponse? =
        withContext(Dispatchers.IO) {
            val response = apiService.getCurrentUserProfile("Bearer $accessToken").execute()
            return@withContext if (response.isSuccessful) {
                response.body()
            } else {
               null
            }
        }

    override suspend fun getTopTracks(
        accessToken: String,
        timeRange: String,
        limit: Int
    ): TopTracksResponse? = withContext(Dispatchers.IO) {
        val response = apiService.getTopTracks(
            token = "Bearer $accessToken",
            timeRange = timeRange,
            limit = limit
        ).execute()
        return@withContext if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getNextPage(accessToken: String, url: String): TopTracksResponse? =
        withContext(Dispatchers.IO) {
            val response = apiService.getNextPage(
                token = "Bearer $accessToken",
                url = url
            ).execute()
            return@withContext if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
}