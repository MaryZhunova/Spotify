package com.example.spotify.data

import com.example.spotify.data.converter.TopArtistsResponseToInfoConverter
import com.example.spotify.data.converter.TopTracksResponseToInfoConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.domain.security.SecurityRepository
import com.example.spotify.models.data.TopArtistsInfo
import com.example.spotify.models.data.TopTracksInfo
import com.example.spotify.models.data.TrackInfo
import com.example.spotify.models.data.UserProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Реализация репозитория для получения статистики пользователя и информации о треках и исполнителях из Spotify
 *
 * @constructor
 * @param apiMapper маппер для преобразования данных из API в модели
 * @param userProfileConverter конвертер для преобразования ответа API о пользователе в модель [UserProfileInfo]
 * @param topTracksConverter конвертер для преобразования ответа API о треках в модель [TopTracksInfo]
 * @param topArtistsConverter конвертер для преобразования ответа API об исполнителях в модель [TopArtistsInfo]
 * @param securityRepository репозиторий для работы с токенами доступа
 */
class SpotifyStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyStatsApiMapper,
    private val userProfileConverter: UserProfileResponseToInfoConverter,
    private val topTracksConverter: TopTracksResponseToInfoConverter,
    private val topArtistsConverter: TopArtistsResponseToInfoConverter,
    private val securityRepository: SecurityRepository,
    private val trackConverter: TrackResponseToInfoConverter
) : SpotifyStatsRepository {

    override suspend fun getCurrentUserProfile(): UserProfileInfo =
        withContext(Dispatchers.IO) {
            val token = securityRepository.getAccessToken()
            val userProfileResponse = apiMapper.getCurrentUserProfile(token)
            userProfileConverter.convert(userProfileResponse)
        }

    override suspend fun getTopTracks(timeRange: String, limit: Int): TopTracksInfo =
        withContext(Dispatchers.IO) {
            val token = securityRepository.getAccessToken()
            val topTracksResponse = apiMapper.getTopTracks(token, timeRange, limit)
            topTracksConverter.convert(topTracksResponse)
        }

    override suspend fun getTopTracksNextPage(url: String): TopTracksInfo =
        withContext(Dispatchers.IO) {
            val token = securityRepository.getAccessToken()
            val topTracksResponse = apiMapper.getTopTracksNextPage(token, url)
            topTracksConverter.convert(topTracksResponse)
        }

    override suspend fun getTopArtists(timeRange: String, limit: Int): TopArtistsInfo =
        withContext(Dispatchers.IO) {
            val token = securityRepository.getAccessToken()
            val topArtistsResponse = apiMapper.getTopArtists(token, timeRange, limit)
            topArtistsConverter.convert(topArtistsResponse)
        }

    override suspend fun getTopArtistsNextPage(url: String): TopArtistsInfo =
        withContext(Dispatchers.IO) {
            val token = securityRepository.getAccessToken()
            val topArtistsResponse = apiMapper.getTopArtistsNextPage(token, url)
            topArtistsConverter.convert(topArtistsResponse)
        }

    override suspend fun getArtistsTopTracks(id: String): List<TrackInfo> =
        withContext(Dispatchers.IO) {
            val token = securityRepository.getAccessToken()
            apiMapper.getArtistsTopTracks(token,id).tracks.map(trackConverter::convert)
        }
}