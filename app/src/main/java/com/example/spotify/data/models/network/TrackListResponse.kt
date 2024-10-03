package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Ответ на запрос самых популярных треков исполнителя
 *
 * @property tracks список треков, каждый из которых представлен объектом `TrackResponse`
 */
data class TrackListResponse(
    @SerializedName("tracks") val tracks: List<TrackResponse>
)

/**
 * Ответ, содержащий список аудиохарактеристик треков.
 *
 * @property audioFeatures Список объектов [AudioFeaturesResponse], представляющих аудиохарактеристики треков.
 */
data class AudioFeaturesListResponse(
    @SerializedName("audio_features") val audioFeatures: List<AudioFeaturesResponse>
)

/**
 * Ответ на запрос самых популярных треков исполнителя
 *
 * @property tracks список треков, каждый из которых представлен объектом `TrackResponse`
 */
data class GenreListResponse(
    @SerializedName("genres") val genres: List<String>
)
