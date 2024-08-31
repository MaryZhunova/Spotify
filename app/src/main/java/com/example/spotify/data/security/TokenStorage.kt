package com.example.spotify.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class TokenStorage @Inject constructor(context: Context) {

    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun storeAccessToken(token: String) {
        sharedPreferences.edit().putString(PREFS_KEY, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(PREFS_KEY, null)
    }

    fun removeAccessToken() {
        sharedPreferences.edit().remove(PREFS_KEY).apply()
    }

    companion object {
        private const val PREFS_KEY = "access_token"
        private const val PREFS_NAME = "secure_prefs"
    }

}