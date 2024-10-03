package com.example.spotify.data.network.mappers

import com.example.spotify.data.models.network.ArtistResponse
import com.example.spotify.data.models.network.PaginatedResponse
import com.example.spotify.data.models.network.TrackResponse
import com.example.spotify.data.models.network.UserProfileResponse
import com.example.spotify.data.network.api.SpotifyUserStatsApiService
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
 * Тесты [SpotifyUserStatsApiMapperImpl]
 */
@ExperimentalCoroutinesApi
class SpotifyUserStatsApiMapperImplTest {

    private val apiService = mockk<SpotifyUserStatsApiService>()
    private val mapper = SpotifyUserStatsApiMapperImpl(apiService)

    @Test
    fun `test getCurrentUserProfile with successful response`() = runTest {
        val accessToken = "access_token"
        val mockResponse = mockk<UserProfileResponse>()
        val mockCall = mockk<Call<UserProfileResponse>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.getCurrentUserProfile(any()) } returns mockCall

        val result = mapper.getCurrentUserProfile(accessToken)

        assertThat(result).isEqualTo(mockResponse)
        coVerify { apiService.getCurrentUserProfile("Bearer $accessToken") }
    }

    @Test
    fun `test getCurrentUserProfile with successful response but null body`() = runTest {
        val accessToken = "access_token"
        val mockCall = mockk<Call<UserProfileResponse>>()
        every { mockCall.execute() } returns Response.success(null)
        coEvery { apiService.getCurrentUserProfile(any()) } returns mockCall

        try {
            mapper.getCurrentUserProfile(accessToken)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(IllegalStateException::class.java)
            assertThat(e.message).isEqualTo("Required value was null.")
        }
        coVerify { apiService.getCurrentUserProfile("Bearer $accessToken") }
    }

    @Test
    fun `test getCurrentUserProfile with error response`() = runTest {
        val accessToken = "access_token"
        val mockCall = mockk<Call<UserProfileResponse>>()
        every { mockCall.execute() } returns Response.error(500, "error".toResponseBody())
        coEvery { apiService.getCurrentUserProfile(any()) } returns mockCall

        try {
            mapper.getCurrentUserProfile(accessToken)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
        coVerify { apiService.getCurrentUserProfile("Bearer $accessToken") }
    }

    @Test
    fun `test getTopTracks with successful response`() = runTest {
        val accessToken = "access_token"
        val timeRange = "short_term"
        val limit = 10
        val mockResponse = mockk<PaginatedResponse<TrackResponse>>()
        val mockCall = mockk<Call<PaginatedResponse<TrackResponse>>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.getTopTracks(any(), any(), any()) } returns mockCall

        val result = mapper.getTopTracks(accessToken, timeRange, limit)

        assertThat(result).isEqualTo(mockResponse)
        coVerify { apiService.getTopTracks("Bearer $accessToken", timeRange, limit) }
    }

    @Test
    fun `test getTopTracks with successful response but null body`() = runTest {
        val accessToken = "access_token"
        val timeRange = "short_term"
        val limit = 10
        val mockCall = mockk<Call<PaginatedResponse<TrackResponse>>>()
        every { mockCall.execute() } returns Response.success(null)
        coEvery { apiService.getTopTracks(any(), any(), any()) } returns mockCall

        try {
            mapper.getTopTracks(accessToken, timeRange, limit)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(IllegalStateException::class.java)
            assertThat(e.message).isEqualTo("Required value was null.")
        }
        coVerify { apiService.getTopTracks("Bearer $accessToken", timeRange, limit) }
    }

    @Test
    fun `test getTopTracks with error response`() = runTest {
        val accessToken = "access_token"
        val timeRange = "short_term"
        val limit = 10
        val mockCall = mockk<Call<PaginatedResponse<TrackResponse>>>()
        every { mockCall.execute() } returns Response.error(500, "error".toResponseBody())
        coEvery { apiService.getTopTracks(any(), any(), any()) } returns mockCall

        try {
            mapper.getTopTracks(accessToken, timeRange, limit)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
        coVerify { apiService.getTopTracks("Bearer $accessToken", timeRange, limit) }
    }

    @Test
    fun `test getTopTracksNextPage with successful response`() = runTest {
        val accessToken = "access_token"
        val url = "http://example.com/next"
        val mockResponse = mockk<PaginatedResponse<TrackResponse>>()
        val mockCall = mockk<Call<PaginatedResponse<TrackResponse>>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.getTopTracksNextPage(any(), any()) } returns mockCall

        val result = mapper.getTopTracksNextPage(accessToken, url)

        assertThat(result).isEqualTo(mockResponse)
        coVerify { apiService.getTopTracksNextPage("Bearer $accessToken", url) }
    }

    @Test
    fun `test getTopTracksNextPage with successful response but null body`() = runTest {
        val accessToken = "access_token"
        val url = "http://example.com/next"
        val mockCall = mockk<Call<PaginatedResponse<TrackResponse>>>()
        every { mockCall.execute() } returns Response.success(null)
        coEvery { apiService.getTopTracksNextPage(any(), any()) } returns mockCall

        try {
            mapper.getTopTracksNextPage(accessToken, url)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(IllegalStateException::class.java)
            assertThat(e.message).isEqualTo("Required value was null.")
        }
        coVerify { apiService.getTopTracksNextPage("Bearer $accessToken", url) }
    }

    @Test
    fun `test getTopTracksNextPage with error response`() = runTest {
        val accessToken = "access_token"
        val url = "http://example.com/next"
        val mockCall = mockk<Call<PaginatedResponse<TrackResponse>>>()
        every { mockCall.execute() } returns Response.error(500, "error".toResponseBody())
        coEvery { apiService.getTopTracksNextPage(any(), any()) } returns mockCall

        try {
            mapper.getTopTracksNextPage(accessToken, url)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
        coVerify { apiService.getTopTracksNextPage("Bearer $accessToken", url) }
    }

    @Test
    fun `test getTopArtists with successful response`() = runTest {
        val accessToken = "access_token"
        val timeRange = "short_term"
        val limit = 10
        val mockResponse = mockk<PaginatedResponse<ArtistResponse>>()
        val mockCall = mockk<Call<PaginatedResponse<ArtistResponse>>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.getTopArtists(any(), any(), any()) } returns mockCall

        val result = mapper.getTopArtists(accessToken, timeRange, limit)

        assertThat(result).isEqualTo(mockResponse)
        coVerify { apiService.getTopArtists("Bearer $accessToken", timeRange, limit) }
    }

    @Test
    fun `test getTopArtists with successful response but null body`() = runTest {
        val accessToken = "access_token"
        val timeRange = "short_term"
        val limit = 10
        val mockCall = mockk<Call<PaginatedResponse<ArtistResponse>>>()
        every { mockCall.execute() } returns Response.success(null)
        coEvery { apiService.getTopArtists(any(), any(), any()) } returns mockCall

        try {
            mapper.getTopArtists(accessToken, timeRange, limit)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(IllegalStateException::class.java)
            assertThat(e.message).isEqualTo("Required value was null.")
        }
        coVerify { apiService.getTopArtists("Bearer $accessToken", timeRange, limit) }
    }

    @Test
    fun `test getTopArtists with error response`() = runTest {
        val accessToken = "access_token"
        val timeRange = "short_term"
        val limit = 10
        val mockCall = mockk<Call<PaginatedResponse<ArtistResponse>>>()
        every { mockCall.execute() } returns Response.error(500, "error".toResponseBody())
        coEvery { apiService.getTopArtists(any(), any(), any()) } returns mockCall

        try {
            mapper.getTopArtists(accessToken, timeRange, limit)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
        coVerify { apiService.getTopArtists("Bearer $accessToken", timeRange, limit) }
    }

    @Test
    fun `test getTopArtistsNextPage with successful response`() = runTest {
        val accessToken = "access_token"
        val url = "http://example.com/next"
        val mockResponse = mockk<PaginatedResponse<ArtistResponse>>()
        val mockCall = mockk<Call<PaginatedResponse<ArtistResponse>>>()
        every { mockCall.execute() } returns Response.success(mockResponse)
        coEvery { apiService.getTopArtistsNextPage(any(), any()) } returns mockCall

        val result = mapper.getTopArtistsNextPage(accessToken, url)

        assertThat(result).isEqualTo(mockResponse)
        coVerify { apiService.getTopArtistsNextPage("Bearer $accessToken", url) }
    }

    @Test
    fun `test getTopArtistsNextPage with successful response but null body`() = runTest {
        val accessToken = "access_token"
        val url = "http://example.com/next"
        val mockCall = mockk<Call<PaginatedResponse<ArtistResponse>>>()
        every { mockCall.execute() } returns Response.success(null)
        coEvery { apiService.getTopArtistsNextPage(any(), any()) } returns mockCall

        try {
            mapper.getTopArtistsNextPage(accessToken, url)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(IllegalStateException::class.java)
            assertThat(e.message).isEqualTo("Required value was null.")
        }
        coVerify { apiService.getTopArtistsNextPage("Bearer $accessToken", url) }
    }

    @Test
    fun `test getTopArtistsNextPage with error response`() = runTest {
        val accessToken = "access_token"
        val url = "http://example.com/next"
        val mockCall = mockk<Call<PaginatedResponse<ArtistResponse>>>()
        every { mockCall.execute() } returns Response.error(500, "error".toResponseBody())
        coEvery { apiService.getTopArtistsNextPage(any(), any()) } returns mockCall

        try {
            mapper.getTopArtistsNextPage(accessToken, url)
        } catch (e: Throwable) {
            assertThat(e).isInstanceOf(Exception::class.java)
        }
        coVerify { apiService.getTopArtistsNextPage("Bearer $accessToken", url) }
    }
}
