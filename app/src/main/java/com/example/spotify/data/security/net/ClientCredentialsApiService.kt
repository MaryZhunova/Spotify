package com.example.spotify.data.security.net

import com.example.spotify.models.data.net.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ClientCredentialsApiService {

    @FormUrlEncoded
    @POST("api/token")
    fun getClientCredentialsToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): Call<AccessTokenResponse>
}