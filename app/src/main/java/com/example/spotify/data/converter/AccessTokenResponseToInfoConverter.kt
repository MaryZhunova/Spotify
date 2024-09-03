package com.example.spotify.data.converter

import com.example.spotify.models.data.AccessTokenInfo
import com.example.spotify.models.data.net.AccessTokenResponse

/**
 * Конвертер сетевой модели [AccessTokenResponse] в дата модель [AccessTokenInfo]
 */
class AccessTokenResponseToInfoConverter {

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
            expiresAt = from.expiresIn * 1000 + System.currentTimeMillis()
        )
}
