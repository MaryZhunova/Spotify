package com.example.spotify.data.converter

import com.example.spotify.domain.models.UserProfileInfo
import com.example.spotify.data.models.network.UserProfileResponse
import java.util.Locale

/**
 * Конвертер сетевой модели [UserProfileResponse] в дата модель [UserProfileInfo]
 */
class UserProfileResponseToInfoConverter {

    /**
     * Конвертирует [UserProfileResponse] в [UserProfileInfo]
     *
     * @param from данные для конвертации
     */
    fun convert(from: UserProfileResponse): UserProfileInfo =
        UserProfileInfo(
            id = from.id,
            displayName = from.displayName,
            email = from.email,
            image = from.images.lastOrNull()?.url,
            country = convertCountry(from.country).orEmpty(),
            product = from.product
        )

    private fun convertCountry(code: String): String? {
        val locale = Locale("", code)
        return locale.displayCountry.takeIf { it != UNKNOWN_REGION }
    }

    private companion object {
        const val UNKNOWN_REGION = "Unknown Region"
    }
}
