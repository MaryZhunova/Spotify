package com.example.spotify.data.net

import com.example.spotify.models.data.net.ArtistsTopTracksResponse
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
}