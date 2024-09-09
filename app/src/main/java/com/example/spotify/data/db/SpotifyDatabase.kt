package com.example.spotify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spotify.data.converter.AlbumInfoTypeConverter
import com.example.spotify.data.converter.ArtistListTypeConverter

@Database(entities = [TrackEntity::class, ArtistEntity::class], version = 1)
@TypeConverters(AlbumInfoTypeConverter::class, ArtistListTypeConverter::class)
abstract class SpotifyDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao

    abstract fun trackDao(): TrackDao
}