package com.example.spotify.hilt

import com.example.spotify.data.network.api.SpotifyInfoApiService
import com.example.spotify.data.network.api.SpotifyUserStatsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL_API = "https://api.spotify.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyUserStatsApiService(okHttpClient: OkHttpClient): SpotifyUserStatsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyUserStatsApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideSpotifyInfoApiService(okHttpClient: OkHttpClient): SpotifyInfoApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyInfoApiService::class.java)
    }
}