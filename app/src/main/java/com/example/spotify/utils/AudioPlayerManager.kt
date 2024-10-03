package com.example.spotify.utils

import android.media.MediaPlayer
import android.util.Log
import com.example.spotify.domain.models.TrackInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

/**
 * Управляет воспроизведением треков
 */
class AudioPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private val _currentTrack = MutableStateFlow<TrackInfo?>(null)

    /**
     * Воспроизводимый трек
     */
    val currentTrack: StateFlow<TrackInfo?>
        get() = _currentTrack

    /**
     * Начинает воспроизведение указанного трека.
     *
     * Если уже воспроизводится другой трек, он будет остановлен.
     *
     * @param track Трек для воспроизведения.
     */
    fun play(track: TrackInfo) {
        if (_currentTrack.value != null && _currentTrack.value != track) {
            stop()
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setOnCompletionListener {
                    _currentTrack.value = null
                }
                setOnErrorListener { _, _, _ ->
                    stop()
                    true
                }
            }
        }

        try {
            mediaPlayer?.apply {
                reset()
                setDataSource(track.previewUrl)
                prepareAsync()
                setOnPreparedListener { start() }
                _currentTrack.value = track
            }
        } catch (e: IOException) {
            Log.d("AudioPlayerManager", "${e.message}")
        }
    }

    /**
     * Останавливает воспроизведение текущего трека.
     */
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.reset()
        }
        _currentTrack.value = null
    }

    /**
     * Освобождает ресурсы, связанные с MediaPlayer.
     * Останавливает воспроизведение и очищает текущий трек.
     */
    fun release() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            reset()
            release()
        }
        mediaPlayer = null
        _currentTrack.value = null
    }
}
