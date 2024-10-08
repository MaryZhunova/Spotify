package com.example.spotify.domain.models

data class AudioFeaturesInfo(
    val acousticness: Double,
    val analysis_url: String,
    val danceability: Double,
    val duration_ms: Int,
    val energy: Double,
    val id: String,
    val instrumentalness: Double,
    val key: Int,
    val liveness: Double,
    val loudness: Double,
    val mode: Int,
    val speechiness: Double,
    val tempo: Double,
    val time_signature: Int,
    val track_href: String,
    val type: String,
    val uri: String,
    val valence: Double
)