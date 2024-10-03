package com.example.spotify.hilt

import android.content.Context
import com.example.spotify.data.storage.FileStorageImpl
import com.example.spotify.data.storage.SpotifyInfoStorage
import com.example.spotify.data.storage.SpotifyInfoStorageImpl
import com.example.spotify.data.storage.SpotifyUserInfoStorage
import com.example.spotify.data.storage.SpotifyUserInfoStorageImpl
import com.example.spotify.data.storage.SpotifyUserStatsStorage
import com.example.spotify.data.storage.SpotifyUserStatsStorageImpl
import com.example.spotify.utils.TimeSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    private const val USER_CACHE_NAME = "user_data"
    private const val USER_STATS_CACHE_NAME = "user_stats_data"
    private const val INFO_CACHE_NAME = "info"

    @Provides
    @Singleton
    fun provideSpotifyUserStatsStorage(
        @ApplicationContext context: Context,
        timeSource: TimeSource
    ): SpotifyUserStatsStorage {

        val fileStorage = FileStorageImpl.Builder()
            .setDirectoryName(USER_STATS_CACHE_NAME)
            .setTimeout(7, TimeUnit.DAYS)
            .build(context, timeSource)

        return SpotifyUserStatsStorageImpl(fileStorage)
    }

    @Provides
    @Singleton
    fun provideSpotifyUserInfoStorage(
        @ApplicationContext context: Context,
        timeSource: TimeSource
    ): SpotifyUserInfoStorage {

        val fileStorage = FileStorageImpl.Builder()
            .setDirectoryName(USER_CACHE_NAME)
            .build(context, timeSource)

        return SpotifyUserInfoStorageImpl(fileStorage)
    }

    @Provides
    @Singleton
    fun provideSpotifyInfoStorage(
        @ApplicationContext context: Context,
        timeSource: TimeSource
    ): SpotifyInfoStorage {

        val fileStorage = FileStorageImpl.Builder()
            .setDirectoryName(INFO_CACHE_NAME)
            .build(context, timeSource)

        return SpotifyInfoStorageImpl(fileStorage)
    }
}