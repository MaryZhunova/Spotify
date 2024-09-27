package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.ArtistsTopTracksResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse
import com.example.spotify.data.network.api.SpotifyInfoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Реализация интерфейса [SpotifyInfoApiMapper],
 * которая использует [SpotifyInfoApiService] для выполнения запросов к API Spotify
 *
 * @constructor
 * @param apiService Сервис для выполнения запросов к API Spotify
 */
class SpotifyInfoApiMapperImpl @Inject constructor(
    private val apiService: SpotifyInfoApiService
) : SpotifyInfoApiMapper {

    override suspend fun getArtistsTopTracks(
        accessToken: String,
        id: String
    ): ArtistsTopTracksResponse = withContext(Dispatchers.IO) {
        val response = apiService.getArtistsTopTracks(
            token = "Bearer $accessToken",
            id = id
        ).execute()
        return@withContext if (response.isSuccessful) {
            checkNotNull(response.body())
        } else {
            throw Exception()
        }
    }

    override suspend fun getTracksAudioFeatures(
        accessToken: String,
        ids: String
    ): AudioFeaturesListResponse = withContext(Dispatchers.IO) {
        val response = apiService.getTracksAudioFeatures(
            token = "Bearer $accessToken",
            ids = ids
        ).execute()
        return@withContext if (response.isSuccessful) {
            checkNotNull(response.body())
        } else {
            throw Exception()
        }
    }
}