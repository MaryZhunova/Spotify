package com.example.spotify.hilt

import com.example.spotify.data.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.data.converter.TopArtistsResponseToInfoConverter
import com.example.spotify.data.converter.TopTracksResponseToInfoConverter
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
    fun provideTopTracksResponseToInfoConverter(
        trackResponseToInfoConverter: TrackResponseToInfoConverter
    ): TopTracksResponseToInfoConverter {
        return TopTracksResponseToInfoConverter(trackResponseToInfoConverter)
    }

    @Provides
    @Singleton
    fun provideTopArtistsResponseToInfoConverter(): TopArtistsResponseToInfoConverter {
        return TopArtistsResponseToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideAccessTokenResponseToInfoConverter(): AccessTokenResponseToInfoConverter {
        return AccessTokenResponseToInfoConverter()
    }
}