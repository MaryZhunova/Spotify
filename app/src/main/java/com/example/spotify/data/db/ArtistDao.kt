package com.example.spotify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtistDao {
    @Query("SELECT * FROM $ARTIST_TABLE_NAME")
    fun getAll(): List<ArtistEntity>

    @Query("SELECT * FROM $ARTIST_TABLE_NAME WHERE $FIELD_ARTIST_ID IN (:artistsIds)")
    fun getByIds(vararg artistsIds: String): List<ArtistEntity>

    @Query("SELECT * FROM $ARTIST_TABLE_NAME WHERE $FIELD_ARTIST_ID = :artistId")
    fun getById(artistId: String): ArtistEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: ArtistEntity)

    @Query("DELETE FROM $ARTIST_TABLE_NAME")
    fun deleteAll()
}