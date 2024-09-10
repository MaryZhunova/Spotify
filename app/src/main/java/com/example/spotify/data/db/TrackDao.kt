package com.example.spotify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg track: TrackEntity)

    @Query("SELECT * FROM $TRACK_TABLE_NAME WHERE :artistId IN ($FIELD_TRACK_ARTISTS_ID)")
    fun findTracksByArtistId(artistId: String): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM $TRACK_TABLE_NAME WHERE $FIELD_TRACK_NAME = :trackId)")
    fun isTrackInDatabase(trackId: String): Boolean

    @Query("SELECT * FROM $TRACK_TABLE_NAME")
    fun getAllTracks(): List<TrackEntity>

    @Query("DELETE FROM $TRACK_TABLE_NAME")
    fun deleteAll()
}