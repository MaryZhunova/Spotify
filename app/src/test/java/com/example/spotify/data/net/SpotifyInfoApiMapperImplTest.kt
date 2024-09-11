package com.example.spotify.data.net

import com.example.spotify.models.data.net.ArtistsTopTracksResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response

/**
 * Тесты [SpotifyInfoApiMapperImpl]
 */
@ExperimentalCoroutinesApi
class SpotifyInfoApiMapperImplTest {

    private val apiService = mockk<SpotifyInfoApiService>()
    private val mapper = SpotifyInfoApiMapperImpl(apiService)

    @Test
    fun `test getArtistsTopTracks with successful response`() = runTest {
        val accessToken = "access_token"
        val artistId = "artist_id"
        val mockResponse = mockk<ArtistsTopTracksResponse>()
        val mockCall = mockk<Call<ArtistsTopTracksResponse>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.getArtistsTopTracks(any(), any()) } returns mockCall

        val result = mapper.getArtistsTopTracks(accessToken, artistId)

        assertThat(result).isEqualTo(mockResponse)
        coVerify { apiService.getArtistsTopTracks("Bearer $accessToken", artistId) }
    }

    @Test
    fun `test getArtistsTopTracks with successful response but null body`() = runTest {
        val accessToken = "access_token"
        val artistId = "artist_id"
        val mockCall = mockk<Call<ArtistsTopTracksResponse>>()
        every { mockCall.execute() } returns Response.success(null)
        coEvery { apiService.getArtistsTopTracks(any(), any()) } returns mockCall

        try {
            mapper.getArtistsTopTracks(accessToken, artistId)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(IllegalStateException::class.java)
            assertThat(e.message).isEqualTo("Required value was null.")
        }
        coVerify { apiService.getArtistsTopTracks("Bearer $accessToken", artistId) }
    }

    @Test
    fun `test getArtistsTopTracks with error response`() = runTest {
        val accessToken = "access_token"
        val artistId = "artist_id"
        val mockCall = mockk<Call<ArtistsTopTracksResponse>>()
        every { mockCall.execute() } returns Response.error(500, "error".toResponseBody())
        coEvery { apiService.getArtistsTopTracks(any(), any()) } returns mockCall

        try {
            mapper.getArtistsTopTracks(accessToken, artistId)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
        coVerify { apiService.getArtistsTopTracks("Bearer $accessToken", artistId) }
    }
}
