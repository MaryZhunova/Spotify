package com.example.spotify.data.converter

import androidx.room.TypeConverter
import com.example.spotify.models.data.AlbumInfo
import com.google.gson.Gson

class AlbumInfoTypeConverter {

    @TypeConverter
    fun fromAlbumInfo(albumInfo: AlbumInfo): String {
        val gson = Gson()
        return gson.toJson(albumInfo)
    }

    @TypeConverter
    fun toAlbumInfo(albumInfoString: String): AlbumInfo {
        return Gson().fromJson(albumInfoString, AlbumInfo::class.java)
    }
}