package com.example.spotify.hilt

import com.example.spotify.data.converter.ArtistEntityToInfoConverter
import com.example.spotify.data.converter.ArtistResponseToEntityConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
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
object ConverterModule {

    @Provides
    @Singleton
    fun provideUserProfileResponseToInfoConverter(): UserProfileResponseToInfoConverter {
        return UserProfileResponseToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideTrackResponseToInfoConverter(): TrackResponseToInfoConverter {
        return TrackResponseToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideTopArtistsEntityToInfoConverter(): ArtistEntityToInfoConverter {
        return ArtistEntityToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideArtistResponseToEntityConverter(): ArtistResponseToEntityConverter {
        return ArtistResponseToEntityConverter()
    }
}