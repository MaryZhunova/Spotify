package com.example.spotify.domain

import com.example.spotify.domain.auth.AuthRepository
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.GenreInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.domain.models.UserProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Интерактор для получения информации о треках и исполнителях из Spotify
 */
class SpotifyInteractorImpl @Inject constructor(
    private val statsRepository: SpotifyUserStatsRepository,
    private val infoRepository: SpotifyInfoRepository,
    private val authRepository: AuthRepository
) : SpotifyInteractor {

    private val accessCode: String by lazy { authRepository.getAccessToken() }

    override suspend fun getArtistsTopTracks(id: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            infoRepository.getArtistsTopTracks(accessCode, id)
        }

    override suspend fun getArtistsInfo(id: String): ArtistInfo = withContext(Dispatchers.IO) {
        statsRepository.getArtistsInfo(id)
    }

    override suspend fun getCurrentUserProfile(): UserProfileInfo = withContext(Dispatchers.IO) {
        statsRepository.getCurrentUserProfile(accessCode)
    }

    override suspend fun getTopTracks(timeRange: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            statsRepository
                .getTopTracks(accessCode, timeRange)
                .addAudioFeatures()
        }

    override suspend fun getTopTracksByArtistId(id: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            statsRepository
                .getTopTracksByArtistId(id)
                .addAudioFeatures()
        }

    override suspend fun getTopArtists(timeRange: String): List<ArtistInfo> =
        withContext(Dispatchers.IO) {
            val accessCode = authRepository.getAccessToken()
            statsRepository.getTopArtists(accessCode, timeRange)
        }

    override suspend fun getTopGenres(timeRange: String): List<GenreInfo> =
        withContext(Dispatchers.IO) {
            val accessCode = authRepository.getAccessToken()
            statsRepository.getTopGenres(accessCode, timeRange)
        }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        statsRepository.clear()
    }

    private suspend fun List<TrackInfo>.addAudioFeatures(): List<TrackInfo> {
        val featuresList = infoRepository.getTracksAudioFeatures(accessCode, this.map { it.id })
        return this.map { track -> track.copy(audioFeatures = featuresList.firstOrNull { it.id == track.id }) }
    }
}