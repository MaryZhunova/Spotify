package com.example.spotify.hilt

import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.data.net.SpotifyStatsApiMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindSpotifyStatsApiMapper(
        spotifyStatsApiMapperImpl: SpotifyStatsApiMapperImpl
    ): SpotifyStatsApiMapper
}