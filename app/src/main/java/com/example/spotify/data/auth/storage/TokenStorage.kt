package com.example.spotify.data.auth.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.spotify.domain.models.auth.AccessTokenInfo
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Хранилище для безопасного хранения и управления токенами доступа
 * Использует шифрование для хранения токенов доступа в EncryptedSharedPreferences
 *
 * @constructor
 * @param context Контекст приложени для доступа к SharedPreferences
 */
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

    /**
     * Сохраняет токен доступа в хранилище
     *
     * @param response информация о токене доступа
     */
    fun storeAccessToken(response: AccessTokenInfo) {
        val json = gson.toJson(response)
        sharedPreferences.edit().putString(PREFS_KEY, json).apply()
    }

    /**
     * Получает токен доступа из хранилища
     *
     * @return [AccessTokenInfo] токен доступа или null, если токен не найден
     */
    fun getAccessToken(): AccessTokenInfo? {
        val json = sharedPreferences.getString(PREFS_KEY, null) ?: return null
        return gson.fromJson(json, AccessTokenInfo::class.java)
    }

    /**
     * Очищает все данные в хранилище
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_KEY = "access_token"
        private const val PREFS_NAME = "secure_prefs"
    }
}