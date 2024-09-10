package com.example.spotify.hilt

import android.content.Context
import androidx.room.Room
import com.example.spotify.data.db.SpotifyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val SPOTIFY_DATABASE_NAME = "spotifyDatabase"
    @Singleton
    @Provides
    fun provideSpotifyDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        SpotifyDatabase::class.java,
        SPOTIFY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideArtistDao(db: SpotifyDatabase) = db.artistDao()

    @Singleton
    @Provides
    fun provideTrackDao(db: SpotifyDatabase) = db.trackDao()
}