package com.example.spotify.data.converter

import com.example.spotify.models.data.UserProfileInfo
import com.example.spotify.models.data.net.UserProfileResponse
import java.util.Locale

class UserProfileResponseToInfoConverter {
    fun convert(from: UserProfileResponse): UserProfileInfo =
        UserProfileInfo(
            id = from.id,
            displayName = from.displayName,
            email = from.email,
            images = from.images.takeIf { it.isNotEmpty() }?.last()?.url,
            country = convertCountry(from.country).orEmpty(),
            product = from.product
        )

    private fun convertCountry(code: String): String? {
        val locale = Locale("", code)
        return locale.displayCountry
    }
}
