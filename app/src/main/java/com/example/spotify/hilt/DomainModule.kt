package com.example.spotify.hilt

import com.example.spotify.domain.SpotifyInteractor
import com.example.spotify.domain.SpotifyInteractorImpl
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
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindSpotifyInteractor(
        spotifyInteractorImpl: SpotifyInteractorImpl
    ): SpotifyInteractor
}