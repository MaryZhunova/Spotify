package com.example.spotify.data

import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.models.data.UserProfileInfo
import javax.inject.Inject

class SpotifyStatsRepositoryImpl @Inject constructor(
    private val apiMapper: SpotifyStatsApiMapper,
    private val converter: UserProfileResponseToInfoConverter,
) : SpotifyStatsRepository {

    override fun getCurrentUserProfile(accessToken: String, callback: (UserProfileInfo?) -> Unit) {
        apiMapper.getCurrentUserProfile(accessToken) { response ->
            response?.let {
                callback(converter.convert(it))
            } ?: callback(null)
        }
    }
}