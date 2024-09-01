package com.example.spotify.data.converter

import com.example.spotify.models.data.AccessTokenInfo
import com.example.spotify.models.data.net.AccessTokenResponse

class AccessTokenResponseToInfoConverter {
    fun convert(from: AccessTokenResponse): AccessTokenInfo =
        AccessTokenInfo(
            accessToken = from.accessToken,
            refreshToken = from.refreshToken,
            tokenType = from.tokenType,
            expiresAt = from.expiresIn * 1000 + System.currentTimeMillis()
        )
}
