package com.example.spotify.data.converter

import com.example.spotify.models.data.AlbumInfo
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

/**
 * Тесты [AlbumInfoTypeConverter]
 */
class AlbumInfoTypeConverterTest {

    private val converter = AlbumInfoTypeConverter()
    private val gson: Gson = mockk()

    @Test
    fun fromAlbumInfoTest() {
        every { gson.toJson(sampleAlbumInfo) } returns sampleAlbumInfoJson

        val result = converter.fromAlbumInfo(sampleAlbumInfo)

        assertThat(result).isEqualTo(sampleAlbumInfoJson)
    }

    @Test
    fun toAlbumInfoTest() {
        every { gson.fromJson(sampleAlbumInfoJson, AlbumInfo::class.java) } returns sampleAlbumInfo

        val result = converter.toAlbumInfo(sampleAlbumInfoJson)

        assertThat(result).isEqualTo(sampleAlbumInfo)
    }

    private companion object {
        const val sampleAlbumInfoJson = "{\"id\":\"1\",\"name\":\"Test Album\",\"image\":\"http://image.com\"}"

        val sampleAlbumInfo = AlbumInfo(
            id = "1",
            name = "Test Album",
            image = "http://image.com"
        )
    }
}