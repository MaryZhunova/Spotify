package com.example.spotify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spotify.data.converter.AlbumInfoTypeConverter
import com.example.spotify.data.converter.ArtistListTypeConverter

/**
 * База данных
 *
 * @property artistDao DAO для работы с таблицей исполнителей
 * @property trackDao DAO для работы с таблицей треков
 */
@Database(entities = [TrackEntity::class, ArtistEntity::class], version = 1, exportSchema = false)
@TypeConverters(AlbumInfoTypeConverter::class, ArtistListTypeConverter::class)
abstract class SpotifyDatabase : RoomDatabase() {

    /**
     * Получает объект DAO для работы с таблицей исполнителей.
     *
     * @return DAO для работы с данными исполнителей.
     */
    abstract fun artistDao(): ArtistDao

    /**
     * Получает объект DAO для работы с таблицей треков.
     *
     * @return DAO для работы с данными треков.
     */
    abstract fun trackDao(): TrackDao
}