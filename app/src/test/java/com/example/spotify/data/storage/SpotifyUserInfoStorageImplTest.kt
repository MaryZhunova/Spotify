package com.example.spotify.data.storage

import com.example.spotify.domain.models.UserProfileInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Тесты [SpotifyUserInfoStorageImpl]
 */
class SpotifyUserInfoStorageImplTest {

    private lateinit var fileStorage: FileStorage
    private lateinit var userInfoStorage: SpotifyUserInfoStorageImpl
    private val userProfileInfo: UserProfileInfo = mockk()

    @BeforeEach
    fun setUp() {
        fileStorage = mockk()

        userInfoStorage = SpotifyUserInfoStorageImpl(fileStorage)
    }

    @Test
    fun `setCurrentUserInfo should save user info to file storage`() {
        every { fileStorage.put(any(), any<UserProfileInfo>(), any()) } just Runs

        userInfoStorage.setCurrentUserInfo(userProfileInfo)

        verify {
            fileStorage.put("current_user", userProfileInfo, any<UserProfileInfoSerializer>())
        }
    }

    @Test
    fun `getCurrentUserInfo should return user info from file storage`() {
        every {
            fileStorage.get("current_user", any<UserProfileInfoSerializer>())
        } returns userProfileInfo

        val result = userInfoStorage.getCurrentUserInfo()

        assertThat(result).isEqualTo(userProfileInfo)
    }

    @Test
    fun `getCurrentUserInfo should return null if user info is not found`() {
        every { fileStorage.get("current_user", any<UserProfileInfoSerializer>()) } returns null

        val result = userInfoStorage.getCurrentUserInfo()

        assertThat(result).isNull()
    }

    @Test
    fun `clear should invoke clear on file storage`() {
        every { fileStorage.clear() } just Runs

        userInfoStorage.clear()

        verify { fileStorage.clear() }
    }
}