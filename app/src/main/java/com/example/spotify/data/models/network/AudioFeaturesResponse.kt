package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

/**
 * Аудиохарактеристики трека.
 *
 * @property acousticness Уровень акустичности трека (диапазон: 0 - 1).
 * @property analysisUrl URL для анализа трека.
 * @property danceability Уровень танцевальности трека (диапазон: 0 - 1).
 * @property durationMs Длительность трека в миллисекундах.
 * @property energy Уровень энергии трека (диапазон: 0 - 1).
 * @property id Идентификатор трека.
 * @property instrumentalness Уровень инструментальности трека (диапазон: 0 - 1).
 * @property key Ключ трека (музыкальная тональность).
 * @property liveness Уровень живости исполнения (диапазон: 0 - 1).
 * @property loudness Уровень громкости трека (диапазон: -60 - 0).
 * @property mode Режим (0 - минор, 1 - мажор).
 * @property speechiness Уровень речевости трека (диапазон: 0 - 1).
 * @property tempo Темп трека в ударах в минуту (BPM).
 * @property timeSignature Тактовая структура трека.
 * @property trackHref URL для трека.
 * @property type Тип объекта (например, "track").
 * @property uri Уникальный идентификатор трека в Spotify.
 * @property valence Уровень настроения трека (диапазон: 0 - 1).
 */
data class AudioFeaturesResponse(
    @SerializedName("acousticness") val acousticness: Double,
    @SerializedName("analysis_url") val analysisUrl: String,
    @SerializedName("danceability") val danceability: Double,
    @SerializedName("duration_ms") val durationMs: Int,
    @SerializedName("energy") val energy: Double,
    @SerializedName("id") val id: String,
    @SerializedName("instrumentalness") val instrumentalness: Double,
    @SerializedName("key") val key: Int,
    @SerializedName("liveness") val liveness: Double,
    @SerializedName("loudness") val loudness: Double,
    @SerializedName("mode") val mode: Int,
    @SerializedName("speechiness") val speechiness: Double,
    @SerializedName("tempo") val tempo: Double,
    @SerializedName("time_signature") val timeSignature: Int,
    @SerializedName("track_href") val trackHref: String,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("valence") val valence: Double
)