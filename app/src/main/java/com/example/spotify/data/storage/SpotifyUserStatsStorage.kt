package com.example.spotify.data.storage

/**
 * Интерфейс для хранения статистики пользователей Spotify.
 */
interface SpotifyUserStatsStorage {

    /**
     * Получает список идентификаторов по заданному ключу.
     *
     * @param key Ключ, по которому сохраняются идентификаторы.
     * @return Список идентификаторов или null, если данные не найдены.
     */
    fun getIdsList(key: String): List<String>?

    /**
     * Сохраняет список идентификаторов для заданного ключа.
     *
     * @param key Ключ, по которому будут сохранены идентификаторы.
     * @param data Список идентификаторов для сохранения.
     */
    fun setIdsList(key: String, data: List<String>)

    /**
     * Очищает все сохраненные данные.
     */
    fun clear()
}
