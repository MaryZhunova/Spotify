package com.example.spotify.hilt.auth

import com.example.spotify.data.auth.converter.AccessTokenResponseToInfoConverter
import com.example.spotify.utils.TimeSource
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
    fun provideAccessTokenResponseToInfoConverter(
        timeSource: TimeSource
    ): AccessTokenResponseToInfoConverter = AccessTokenResponseToInfoConverter(timeSource)
}