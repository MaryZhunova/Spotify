package com.example.spotify.hilt

import com.example.spotify.data.converter.ArtistEntityToInfoConverter
import com.example.spotify.data.converter.ArtistResponseToEntityConverter
import com.example.spotify.data.converter.ArtistResponseToInfoConverter
import com.example.spotify.data.converter.AudioFeaturesResponseToInfoConverter
import com.example.spotify.data.converter.SearchResponseToInfoConverter
import com.example.spotify.data.converter.TrackEntityToInfoConverter
import com.example.spotify.data.converter.TrackResponseToEntityConverter
import com.example.spotify.data.converter.TrackResponseToInfoConverter
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.db.TrackDao
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
    fun provideTrackResponseToEntityConverter(): TrackResponseToEntityConverter {
        return TrackResponseToEntityConverter()
    }

    @Provides
    @Singleton
    fun provideTrackEntityToInfoConverter(): TrackEntityToInfoConverter {
        return TrackEntityToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideTrackResponseToInfoConverter(trackDao: TrackDao): TrackResponseToInfoConverter {
        return TrackResponseToInfoConverter(trackDao)
    }

    @Provides
    @Singleton
    fun provideArtistResponseToEntityConverter(): ArtistResponseToEntityConverter {
        return ArtistResponseToEntityConverter()
    }

    @Provides
    @Singleton
    fun provideTopArtistsEntityToInfoConverter(): ArtistEntityToInfoConverter {
        return ArtistEntityToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideArtistResponseToInfoConverter(): ArtistResponseToInfoConverter {
        return ArtistResponseToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideSearchResponseToInfoConverter(
        trackInfoConverter: TrackResponseToInfoConverter,
        artistInfoConverter: ArtistResponseToInfoConverter
    ): SearchResponseToInfoConverter {
        return SearchResponseToInfoConverter(trackInfoConverter, artistInfoConverter)
    }

    @Provides
    @Singleton
    fun provideAudioFeaturesResponseToInfoConverter(): AudioFeaturesResponseToInfoConverter {
        return AudioFeaturesResponseToInfoConverter()
    }
}