package com.example.spotify.data.storage

/**
 * Сериализатор для списков строк
 */
class StringListSerializer : Serializer<List<String>> {
    override fun serialize(data: List<String>): ByteArray {
        return data.joinToString(",").toByteArray(Charsets.UTF_8)
    }

    override fun deserialize(data: ByteArray): List<String> {
        return String(data, Charsets.UTF_8).split(",").map { it.trim() }
    }
}