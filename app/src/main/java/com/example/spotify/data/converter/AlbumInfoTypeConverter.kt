package com.example.spotify.data.converter

import androidx.room.TypeConverter
import com.example.spotify.models.data.AlbumInfo
import com.google.gson.Gson
/**
 * Конвертер для преобразования объекта `AlbumInfo` в строку и обратно
 */
class AlbumInfoTypeConverter {

    /**
     * Преобразует объект `AlbumInfo` в строку JSON.
     *
     * @param albumInfo Объект `AlbumInfo`, который нужно преобразовать.
     * @return Строка JSON, представляющая объект `AlbumInfo`.
     */
    @TypeConverter
    fun fromAlbumInfo(albumInfo: AlbumInfo): String {
        val gson = Gson()
        return gson.toJson(albumInfo)
    }

    /**
     * Преобразует строку JSON обратно в объект `AlbumInfo`.
     *
     * @param albumInfoString Строка JSON, представляющая объект `AlbumInfo`.
     * @return Объект `AlbumInfo`, соответствующий строке JSON.
     */
    @TypeConverter
    fun toAlbumInfo(albumInfoString: String): AlbumInfo {
        return Gson().fromJson(albumInfoString, AlbumInfo::class.java)
    }
}
