package com.example.spotify.hilt.security

import com.example.spotify.data.security.AuthRepositoryImpl
import com.example.spotify.data.security.net.SpotifyAuthApiMapper
import com.example.spotify.data.security.net.SpotifyAuthApiMapperImpl
import com.example.spotify.domain.security.AuthRepository
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
abstract class AuthDataModule {

    @Binds
    @Singleton
    abstract fun bindSpotifyAuthApiMapper(
        spotifyAuthApiMapperImpl: SpotifyAuthApiMapperImpl
    ): SpotifyAuthApiMapper

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}