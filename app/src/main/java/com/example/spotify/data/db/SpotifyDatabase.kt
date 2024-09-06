package com.example.spotify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArtistEntity::class], version = 1)
abstract class SpotifyDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao
}