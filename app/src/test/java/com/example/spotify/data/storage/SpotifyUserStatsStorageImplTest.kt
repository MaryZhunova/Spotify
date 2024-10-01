package com.example.spotify.data.storage

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

/**
 * Тесты [SpotifyUserStatsStorageImpl]
 */
class SpotifyUserStatsStorageImplTest {

    private lateinit var fileStorage: FileStorage
    private lateinit var userStatsStorage: SpotifyUserStatsStorageImpl

    @BeforeEach
    fun setUp() {
        fileStorage = mockk()
        userStatsStorage = SpotifyUserStatsStorageImpl(fileStorage)
    }

    @Test
    fun `setIdsList should save IDs list to file storage`() {
        val key = "test_key"
        val idsList = listOf("id1", "id2", "id3")

        every { fileStorage.put(any(), any<List<String>>(), any()) } just Runs

        userStatsStorage.setIdsList(key, idsList)

        verify {
            fileStorage.put(key, idsList, any<StringListSerializer>())
        }
    }

    @Test
    fun `getIdsList should return IDs list from file storage`() {
        val key = "test_key"
        val idsList = listOf("id1", "id2", "id3")

        every { fileStorage.get(key, any<StringListSerializer>()) } returns idsList

        val result = userStatsStorage.getIdsList(key)

        assertThat(result).isEqualTo(idsList)
    }

    @Test
    fun `getIdsList should return null if IDs list is not found`() {
        val key = "test_key"

        every { fileStorage.get(key, any<StringListSerializer>()) } returns null

        val result = userStatsStorage.getIdsList(key)

        assertThat(result).isNull()
    }

    @Test
    fun `clear should invoke clear on file storage`() {
        every { fileStorage.clear() } just Runs

        userStatsStorage.clear()

        verify { fileStorage.clear() }
    }
}