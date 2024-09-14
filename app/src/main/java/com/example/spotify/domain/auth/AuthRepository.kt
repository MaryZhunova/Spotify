package com.example.spotify.domain.auth


/**
 * Интерфейс репозитория для хранения токенов доступа
 */
interface AuthRepository {

    /**
     * Получает токен доступа, используя авторизационный код и URI перенаправления
     *
     * @param accessCode авторизационный ко
     * @param redirectUri URI для перенаправления после получения токена доступа
     * @return токен доступа в виде строки или `null`, если запрос не удался
     */
    suspend fun obtainAccessToken(accessCode: String, redirectUri: String): String?

    /**
     * Получает текущий токен доступа
     *
     * @return текущий токен доступа в виде строки
     */
    suspend fun getAccessToken(): String

    /**
     * Очищает все данные, связанные с токенами доступа
     */
    fun clear()
}