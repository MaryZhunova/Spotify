package com.example.spotify.data.storage

import javax.inject.Inject

/**
 * Реализация интерфейса [SpotifyUserStatsStorage] для хранения статистики пользователей Spotify
 * в файловом хранилище.
 *
 * @param fileStorage Экземпляр [FileStorage], используемый для операций чтения и записи в файл.
 */
class SpotifyUserStatsStorageImpl @Inject constructor(
    private val fileStorage: FileStorage
) : SpotifyUserStatsStorage {

    override fun getIdsList(key: String): List<String>? =
        fileStorage.get(key, StringListSerializer())

    override fun setIdsList(key: String, data: List<String>) {
        fileStorage.put(key, data, StringListSerializer())
    }

    override fun clear() {
        fileStorage.clear()
    }
}