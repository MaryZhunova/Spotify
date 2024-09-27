package com.example.spotify.data.converter

import com.example.spotify.data.models.network.AudioFeaturesResponse
import com.example.spotify.domain.models.AudioFeaturesInfo

class AudioFeaturesResponseToInfoConverter {

    fun convert(from: AudioFeaturesResponse): AudioFeaturesInfo =
        AudioFeaturesInfo(
            acousticness = from.acousticness,
            analysis_url = from.analysisUrl,
            danceability = from.danceability,
            duration_ms = from.durationMs,
            energy = from.energy,
            id = from.id,
            instrumentalness = from.instrumentalness,
            key = from.key,
            liveness = from.liveness,
            loudness = normalizeLoudness(from.loudness),
            mode = from.mode,
            speechiness = from.speechiness,
            tempo = from.tempo,
            time_signature = from.timeSignature,
            track_href = from.trackHref,
            type = from.type,
            uri = from.uri,
            valence = from.valence
        )

   private fun normalizeLoudness(loudness: Double): Double = (loudness + 60) / 60

}