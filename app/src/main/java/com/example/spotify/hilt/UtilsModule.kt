package com.example.spotify.hilt

import com.example.spotify.utils.AudioPlayerManager
import com.example.spotify.utils.TimeSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideTimeSource(): TimeSource {
        return TimeSource()
    }

    @Provides
    @Singleton
    fun provideAudioPlayerManager(): AudioPlayerManager {
        return AudioPlayerManager()
    }
}