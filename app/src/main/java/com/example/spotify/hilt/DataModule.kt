package com.example.spotify.hilt

import com.example.spotify.data.SpotifyInfoRepositoryImpl
import com.example.spotify.data.network.mappers.SpotifyUserStatsApiMapper
import com.example.spotify.data.network.mappers.SpotifyUserStatsApiMapperImpl
import com.example.spotify.domain.SpotifyUserStatsRepository
import com.example.spotify.data.SpotifyUserStatsRepositoryImpl
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapper
import com.example.spotify.data.network.mappers.SpotifyInfoApiMapperImpl
import com.example.spotify.domain.SpotifyInfoRepository
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

    //user stats region
    @Binds
    @Singleton
    abstract fun bindSpotifyUserStatsApiMapper(
        spotifyStatsApiMapperImpl: SpotifyUserStatsApiMapperImpl
    ): SpotifyUserStatsApiMapper

    @Binds
    @Singleton
    abstract fun bindSpotifyStatsRepository(
        spotifyStatsRepositoryImpl: SpotifyUserStatsRepositoryImpl
    ): SpotifyUserStatsRepository

    //info region
    @Binds
    @Singleton
    abstract fun bindSpotifyInfoRepository(
        spotifyInfoRepositoryImpl: SpotifyInfoRepositoryImpl
    ): SpotifyInfoRepository

    @Binds
    @Singleton
    abstract fun bindSpotifyInfoApiMapper(
        spotifyInfoApiMapperImpl: SpotifyInfoApiMapperImpl
    ): SpotifyInfoApiMapper
}