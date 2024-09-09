package com.example.spotify.data.converter

import androidx.room.TypeConverter

class ArtistListTypeConverter {

    @TypeConverter
    fun fromArtistList(artists: List<String>): String {
        return artists.joinToString(",")
    }

    @TypeConverter
    fun toArtistList(artistString: String): List<String> {
        return artistString.split(",")
    }
}