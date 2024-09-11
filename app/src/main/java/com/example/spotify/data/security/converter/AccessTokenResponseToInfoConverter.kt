package com.example.spotify.data.security.converter

import com.example.spotify.models.data.security.AccessTokenInfo
import com.example.spotify.models.data.security.net.AccessTokenResponse
import com.example.spotify.utils.TimeSource
import javax.inject.Inject

/**
 * Конвертер сетевой модели [AccessTokenResponse] в дата модель [AccessTokenInfo]
 *
 * @constructor
 * @param timeSource класс для получения текущего времени
 */
class AccessTokenResponseToInfoConverter @Inject constructor(
    private val timeSource: TimeSource
) {
    /**
     * Конвертирует [AccessTokenResponse] в [AccessTokenInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: AccessTokenResponse): AccessTokenInfo =
        AccessTokenInfo(
            accessToken = from.accessToken,
            refreshToken = from.refreshToken,
            tokenType = from.tokenType,
            expiresAt = from.expiresIn * 1000 + timeSource.getCurrentTime()
        )
}
