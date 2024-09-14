package com.example.spotify.hilt.auth

import com.example.spotify.data.auth.AuthRepositoryImpl
import com.example.spotify.data.auth.net.SpotifyAuthApiMapper
import com.example.spotify.data.auth.net.SpotifyAuthApiMapperImpl
import com.example.spotify.domain.auth.AuthRepository
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