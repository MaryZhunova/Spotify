package com.example.spotify.utils

import android.media.MediaPlayer
import com.example.spotify.domain.models.TrackInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
            }
        }

        mediaPlayer?.apply {
            reset()
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener { start() }
            _currentTrack.value = track
        }
    }

    /**
     * Останавливает воспроизведение текущего трека.
     */
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        _currentTrack.value = null
    }

    /**
     * Освобождает ресурсы, связанные с MediaPlayer.
     * Останавливает воспроизведение и очищает текущий трек.
     */
    fun release() {
        mediaPlayer?.stop()
        _currentTrack.value = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
