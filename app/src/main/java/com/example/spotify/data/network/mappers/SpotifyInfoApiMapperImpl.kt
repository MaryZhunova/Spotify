package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.TrackListResponse
import com.example.spotify.data.models.network.AudioFeaturesListResponse
import com.example.spotify.data.models.network.GenreListResponse
import com.example.spotify.data.models.network.SearchResponse
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
    ): TrackListResponse = withContext(Dispatchers.IO) {
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

    override suspend fun search(
        accessToken: String,
        type: String,
        query: String
    ): SearchResponse = withContext(Dispatchers.IO) {
        val response = apiService.search(
            token = "Bearer $accessToken",
            query = query,
            type = type
        ).execute()
        return@withContext if (response.isSuccessful) {
            checkNotNull(response.body())
        } else {
            throw Exception()
        }
    }

    override suspend fun searchGenres(accessToken: String): GenreListResponse =
        withContext(Dispatchers.IO) {
            val response = apiService.searchGenres(token = "Bearer $accessToken").execute()
            return@withContext if (response.isSuccessful) {
                checkNotNull(response.body())
            } else {
                throw Exception()
            }
        }

    override suspend fun createPlaylist(
        accessToken: String,
        artists: String,
        genres: String,
        tracks: String
    ): TrackListResponse = withContext(Dispatchers.IO) {
        val response = apiService.createPlaylist(
            token = "Bearer $accessToken",
            artists = artists,
            genres = genres,
            tracks = tracks
        ).execute()
        return@withContext if (response.isSuccessful) {
            checkNotNull(response.body())
        } else {
            throw Exception()
        }
    }
}