package com.example.spotify.data.storage

import javax.inject.Inject

/**
 * Реализация интерфейса [SpotifyUserInfoStorage].
 *
 * @param fileStorage Объект [FileStorage], используемый для хранения информации о пользователе.
 */
class SpotifyInfoStorageImpl @Inject constructor(
    private val fileStorage: FileStorage
) : SpotifyInfoStorage {

    override fun getAvailableGenres(): List<String>? =
        fileStorage.get(GENRES_KEY, StringListSerializer())

    override fun setAvailableGenres(genres: List<String>) {
        fileStorage.put(GENRES_KEY, genres, StringListSerializer())
    }

    override fun clear() {
        fileStorage.clear()
    }

    private companion object {
        const val GENRES_KEY = "available_genres"
    }

}