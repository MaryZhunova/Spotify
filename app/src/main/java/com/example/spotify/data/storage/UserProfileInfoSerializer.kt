package com.example.spotify.data.storage

import com.example.spotify.domain.models.UserProfileInfo

/**
 * Сериализатор для объектов [UserProfileInfo]
 */
class UserProfileInfoSerializer : Serializer<UserProfileInfo> {

    override fun serialize(data: UserProfileInfo): ByteArray {
        val serializedString = "${data.id},${data.displayName},${data.email},${data.image ?: ""},${data.country},${data.product}"
        return serializedString.toByteArray(Charsets.UTF_8)
    }

    override fun deserialize(data: ByteArray): UserProfileInfo {
        val content = String(data, Charsets.UTF_8)
        val parts = content.split(",")
        return UserProfileInfo(
            id = parts[0],
            displayName = parts[1],
            email = parts[2],
            image = parts[3].ifEmpty { null },
            country = parts[4],
            product = parts[5]
        )
    }
}