package com.example.spotify.hilt.auth

import android.content.Context
import com.example.spotify.data.auth.TokenStorage
import com.example.spotify.data.auth.net.SpotifyAuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Модуль Dagger-Hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {
    private const val BASE_URL_ACCOUNTS = "https://accounts.spotify.com/"

    @Provides
    @Singleton
    fun provideSpotifyAuthApiService(okHttpClient: OkHttpClient): SpotifyAuthApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_ACCOUNTS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyAuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage {
        return TokenStorage(context)

    }
}