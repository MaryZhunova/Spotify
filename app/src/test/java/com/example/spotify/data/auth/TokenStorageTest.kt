package com.example.spotify.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.spotify.models.data.auth.AccessTokenInfo
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Тесты [TokenStorage]
 */
@ExperimentalCoroutinesApi
class TokenStorageTest {

    private val context: Context = mockk(relaxed = true)
    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private val accessTokenInfo: AccessTokenInfo = mockk()

    private lateinit var tokenStorage: TokenStorage

    @BeforeEach
    fun setUp() {
        mockkStatic(EncryptedSharedPreferences::class)
        mockkConstructor(Gson::class)
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(TEST_PREFS_KEY, any()) } returns editor
        every { editor.clear() } returns editor
        every { editor.apply() } just Runs
        every { anyConstructed<Gson>().toJson(any()) } returns "{}"
        every { anyConstructed<Gson>().toJson(any(), AccessTokenInfo::class.java) } returns "{}"
        every {
            anyConstructed<Gson>().fromJson(
                any<String>(),
                AccessTokenInfo::class.java
            )
        } returns accessTokenInfo

        every {
            EncryptedSharedPreferences.create(
                context,
                TEST_PREFS_NAME,
                any(),
                any(),
                any()
            )
        } returns sharedPreferences

        tokenStorage = TokenStorage(context)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun storeAccessTokenTest() {
        val accessTokenInfo = AccessTokenInfo(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3600 * 1000
        )

        tokenStorage.storeAccessToken(accessTokenInfo)

        verifySequence {
            anyConstructed<Gson>().toJson(accessTokenInfo)
            anyConstructed<Gson>().toJson(accessTokenInfo, AccessTokenInfo::class.java)
            sharedPreferences.edit()
            editor.putString(TEST_PREFS_KEY, "{}")
            editor.apply()
        }
    }

    @Test
    fun getAccessTokenTest() {
        val json = "{}"
        val accessTokenInfo = AccessTokenInfo(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            tokenType = "Bearer",
            expiresAt = 3600 * 1000
        )

        every { sharedPreferences.getString(TEST_PREFS_KEY, null) } returns json
        every {
            anyConstructed<Gson>().fromJson(json, AccessTokenInfo::class.java)
        } returns accessTokenInfo

        assertThat(tokenStorage.getAccessToken()).isEqualTo(accessTokenInfo)

        verifySequence {
            sharedPreferences.getString(TEST_PREFS_KEY, null)
            anyConstructed<Gson>().fromJson(json, AccessTokenInfo::class.java)
        }
    }

    @Test
    fun `getAccessToken should return null if token is not found`() {
        every { sharedPreferences.getString(TEST_PREFS_KEY, null) } returns null

        val result = tokenStorage.getAccessToken()

        assertThat(result).isNull()
        verify { sharedPreferences.getString(TEST_PREFS_KEY, null) }
    }

    @Test
    fun `clear should clear all data in shared preferences`() {
        tokenStorage.clear()

        verifySequence {
            sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }

    private companion object {
        const val TEST_PREFS_KEY = "access_token"
        const val TEST_PREFS_NAME = "secure_prefs"
    }
}
