package com.example.spotify.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.spotify.models.data.AccessTokenInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    private val gson = Gson()

    fun storeAccessToken(response: AccessTokenInfo) {
        val json = gson.toJson(response)
        sharedPreferences.edit().putString(PREFS_KEY, json).apply()
    }

    fun getAccessToken(): AccessTokenInfo? {
        val json = sharedPreferences.getString(PREFS_KEY, null) ?: return null
        return gson.fromJson(json, object : TypeToken<AccessTokenInfo>() {}.type)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_KEY = "access_token"
        private const val PREFS_NAME = "secure_prefs"
    }
}