package com.example.spotify.data.net

import com.example.spotify.models.data.net.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyStatsApiService {

    @GET("v1/me")
    fun getCurrentUserProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>
}