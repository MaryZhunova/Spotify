package com.example.spotify.hilt

import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.spotify.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): SpotifyStatsApiService {
        return retrofit.create(SpotifyStatsApiService::class.java)
    }

    //todo move
    @Provides
    fun provideUserProfileResponseToInfoConverter(): UserProfileResponseToInfoConverter {
        return UserProfileResponseToInfoConverter()
    }
}