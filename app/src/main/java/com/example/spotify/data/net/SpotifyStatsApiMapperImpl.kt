package com.example.spotify.data.net

import com.example.spotify.models.data.net.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SpotifyStatsApiMapperImpl @Inject constructor(
    private val apiService: SpotifyStatsApiService
) : SpotifyStatsApiMapper {

    override fun getCurrentUserProfile(
        accessToken: String,
        callback: (UserProfileResponse?) -> Unit
    ) {
        val call = apiService.getCurrentUserProfile("Bearer $accessToken")
        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}