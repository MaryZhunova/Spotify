package com.example.spotify.data.storage

import com.example.spotify.domain.models.UserProfileInfo
import javax.inject.Inject

/**
 * Реализация интерфейса [SpotifyUserInfoStorage].
 *
 * @param fileStorage Объект [FileStorage], используемый для хранения информации о пользователе.
 */
class SpotifyUserInfoStorageImpl @Inject constructor(
    private val fileStorage: FileStorage
) : SpotifyUserInfoStorage {

    override fun getCurrentUserInfo(): UserProfileInfo? =
        fileStorage.get(CURRENT_USER_KEY, UserProfileInfoSerializer())

    override fun setCurrentUserInfo(info: UserProfileInfo) {
        fileStorage.put(CURRENT_USER_KEY, info, UserProfileInfoSerializer())
    }

    override fun clear() {
        fileStorage.clear()
    }

    private companion object {
        const val CURRENT_USER_KEY = "current_user"
    }

}