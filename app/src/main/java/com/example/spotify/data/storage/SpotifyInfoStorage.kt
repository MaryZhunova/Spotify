package com.example.spotify.data.storage

import com.example.spotify.domain.models.UserProfileInfo

/**
 * Интерфейс для хранения информации о пользователе Spotify.
 *
 * Предоставляет методы для получения, установки и очистки информации о текущем пользователе.
 */
interface SpotifyInfoStorage {

    /**
     * Получает информацию о текущем пользователе из файлового хранилища.
     *
     * @return Объект [UserProfileInfo] с информацией о пользователе или `null`, если информация отсутствует.
     */
    fun getAvailableGenres(): List<String>?

    /**
     * Сохраняет информацию о текущем пользователе в файловом хранилище.
     *
     * @param info Объект [UserProfileInfo], содержащий информацию о пользователе.
     */
    fun setAvailableGenres(genres: List<String>)

    /**
     * Очищает информацию о текущем пользователе.
     */
    fun clear()
}
