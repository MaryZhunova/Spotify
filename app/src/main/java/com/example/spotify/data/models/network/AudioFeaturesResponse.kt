package com.example.spotify.data.models.network

import com.google.gson.annotations.SerializedName

data class AudioFeaturesListResponse(
    @SerializedName("audio_features") val audioFeatures: List<AudioFeaturesResponse>
)

data class AudioFeaturesResponse(
    @SerializedName("acousticness") val acousticness: Double, //Range: 0 - 1
    @SerializedName("analysis_url") val analysisUrl: String,
    @SerializedName("danceability") val danceability: Double, //Range: 0 - 1
    @SerializedName("duration_ms") val durationMs: Int,
    @SerializedName("energy") val energy: Double, //Range: 0 - 1
    @SerializedName("id") val id: String,
    @SerializedName("instrumentalness") val instrumentalness: Double, //Range: 0 - 1
    @SerializedName("key") val key: Int,
    @SerializedName("liveness") val liveness: Double, //Range: 0 - 1
    @SerializedName("loudness") val loudness: Double, //Range: -60 - 0
    @SerializedName("mode") val mode: Int,
    @SerializedName("speechiness") val speechiness: Double, //Range: 0 - 1
    @SerializedName("tempo") val tempo: Double,
    @SerializedName("time_signature") val timeSignature: Int,
    @SerializedName("track_href") val trackHref: String,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("valence") val valence: Double //Range: 0 - 1
)