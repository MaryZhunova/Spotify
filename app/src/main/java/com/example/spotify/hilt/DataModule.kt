package com.example.spotify.hilt

import com.example.spotify.data.net.SpotifyStatsApiMapper
import com.example.spotify.data.net.SpotifyStatsApiMapperImpl
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.data.SpotifyStatsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindSpotifyStatsApiMapper(
        spotifyStatsApiMapperImpl: SpotifyStatsApiMapperImpl
    ): SpotifyStatsApiMapper

    @Binds
    @Singleton
    abstract fun bindSpotifyStatsRepository(
        spotifyStatsRepositoryImpl: SpotifyStatsRepositoryImpl
    ): SpotifyStatsRepository
}