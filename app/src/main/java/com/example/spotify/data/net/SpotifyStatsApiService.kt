package com.example.spotify.data.net

import com.example.spotify.models.data.net.TopTracksResponse
import com.example.spotify.models.data.net.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface SpotifyStatsApiService {

    @GET("v1/me")
    fun getCurrentUserProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>

    @GET("v1/me/top/tracks")
    fun getTopTracks(
        @Header("Authorization") token: String,
        @Query("time_range") timeRange: String = "short_term",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): Call<TopTracksResponse>

    @GET
    fun getNextPage(
        @Header("Authorization") token: String,
        @Url url: String
    ): Call<TopTracksResponse>
}