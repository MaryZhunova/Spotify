package com.example.spotify.utils

import android.media.MediaPlayer
import com.example.spotify.domain.models.TrackInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Тесты [AudioPlayerManager]
 */
class AudioPlayerManagerTest {

    private lateinit var audioPlayerManager: AudioPlayerManager
    private val trackInfo = mockk<TrackInfo>(relaxed = true) {
        every { previewUrl } returns "http://example.com/track"
    }

    @BeforeEach
    fun setUp() {
        audioPlayerManager = AudioPlayerManager()
        mockkConstructor(MediaPlayer::class)
        every { anyConstructed<MediaPlayer>().setOnCompletionListener(any()) } just Runs
        every { anyConstructed<MediaPlayer>().prepareAsync() } just Runs
        every { anyConstructed<MediaPlayer>().reset() } just Runs
        every { anyConstructed<MediaPlayer>().stop() } just Runs
        every { anyConstructed<MediaPlayer>().start() } just Runs
        every { anyConstructed<MediaPlayer>().release() } just Runs
        every { anyConstructed<MediaPlayer>().setDataSource(any<String>()) } just Runs
        every { anyConstructed<MediaPlayer>().setOnPreparedListener(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `play should start playback of the specified track`() = runTest {
        audioPlayerManager.play(trackInfo)

        assertThat(audioPlayerManager.currentTrack.value).isEqualTo(trackInfo)

        verifySequence {
            anyConstructed<MediaPlayer>().setOnCompletionListener(any())
            anyConstructed<MediaPlayer>().reset()
            anyConstructed<MediaPlayer>().setDataSource("http://example.com/track")
            anyConstructed<MediaPlayer>().prepareAsync()
            anyConstructed<MediaPlayer>().setOnPreparedListener(any())
        }
    }

    @Test
    fun `play should stop current track if a different track is played`() = runTest {
        val anotherTrack = mockk<TrackInfo>(relaxed = true) {
            every { previewUrl } returns "http://example.com/another_track"
        }

        audioPlayerManager.play(trackInfo)
        audioPlayerManager.play(anotherTrack)

        assertThat(audioPlayerManager.currentTrack.value).isEqualTo(anotherTrack)

        verify {
            anyConstructed<MediaPlayer>().stop()
            anyConstructed<MediaPlayer>().reset()
            anyConstructed<MediaPlayer>().setDataSource("http://example.com/another_track")
        }
    }

    @Test
    fun `stop should stop playback and clear current track`() = runTest {
        audioPlayerManager.play(trackInfo)
        audioPlayerManager.stop()

        assertThat(audioPlayerManager.currentTrack.value).isNull()
        verify {
            anyConstructed<MediaPlayer>().stop()
            anyConstructed<MediaPlayer>().reset()
        }
    }

    @Test
    fun `release should stop playback and free resources`() = runTest {
        audioPlayerManager.play(trackInfo)
        assertThat(audioPlayerManager.currentTrack.value).isEqualTo(trackInfo)


        audioPlayerManager.release()
        assertThat(audioPlayerManager.currentTrack.value).isNull()

        verify {
            anyConstructed<MediaPlayer>().stop()
            anyConstructed<MediaPlayer>().release()
        }
    }
}