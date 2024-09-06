package com.example.spotify.hilt.security

import com.example.spotify.data.security.converter.AccessTokenResponseToInfoConverter
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
object AuthConverterModule {

    @Provides
    @Singleton
    fun provideAccessTokenResponseToInfoConverter(): AccessTokenResponseToInfoConverter {
        return AccessTokenResponseToInfoConverter()
    }
}