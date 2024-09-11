package com.example.spotify.data.converter

import androidx.room.TypeConverter

/**
 * Конвертер для преобразования списка идентификаторов исполнителей в строку и обратно
 */
class ArtistListTypeConverter {

    /**
     * Преобразует список идентификаторов исполнителей в строку.
     *
     * @param artists Список идентификаторов исполнителей.
     * @return Строка, представляющая список идентификаторов, разделенных запятыми.
     */
    @TypeConverter
    fun fromArtistList(artists: List<String>): String {
        return artists.joinToString(",")
    }

    /**
     * Преобразует строку с идентификаторами исполнителей обратно в список.
     *
     * @param artistString Строка, представляющая список идентификаторов, разделенных запятыми.
     * @return Список идентификаторов исполнителей.
     */
    @TypeConverter
    fun toArtistList(artistString: String): List<String> {
        return artistString.split(",")
    }
}
