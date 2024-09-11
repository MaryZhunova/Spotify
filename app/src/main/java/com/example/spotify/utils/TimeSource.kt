package com.example.spotify.utils

/**
 * Класс для получения текущего времени
 */
class TimeSource {

    /**
     * Получает текущее время в миллисекундах
     *
     * @return текущее время в миллисекундах
     */
    fun getCurrentTime() = System.currentTimeMillis()
}