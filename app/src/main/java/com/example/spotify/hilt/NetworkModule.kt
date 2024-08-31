package com.example.spotify.hilt

import android.content.Context
import com.example.spotify.data.converter.UserProfileResponseToInfoConverter
import com.example.spotify.data.net.SpotifyStatsApiService
import com.example.spotify.data.security.TokenStorage
import com.example.spotify.data.security.net.ClientCredentialsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL_API = "https://api.spotify.com/"
    private const val BASE_URL_ACCOUNTS = "https://accounts.spotify.com/"

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
    fun provideSpotifyStatsApiService(okHttpClient: OkHttpClient): SpotifyStatsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyStatsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideClientCredentialsApiService(okHttpClient: OkHttpClient): ClientCredentialsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_ACCOUNTS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ClientCredentialsApiService::class.java)
    }

    //todo move
    @Provides
    @Singleton
    fun provideUserProfileResponseToInfoConverter(): UserProfileResponseToInfoConverter {
        return UserProfileResponseToInfoConverter()
    }

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage {
        return TokenStorage(context)
    }
}