package com.example.spotify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) для работы с таблицей треков в базе данных
 */
@Dao
interface TrackDao {

    /**
     * Вставляет один или несколько треков в таблицу.
     * Если трек с таким идентификатором уже существует, он будет проигнорирован.
     *
     * @param track треки, которые нужно вставить.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg track: TrackEntity)

    /**
     * Находит треки по идентификатору исполнителя.
     *
     * @param artistId Идентификатор исполнителя, по которому нужно найти треки.
     * @return Список треков
     */
    @Query("SELECT * FROM $TRACK_TABLE_NAME WHERE :artistId IN ($FIELD_TRACK_ARTISTS_ID)")
    fun findTracksByArtistId(artistId: String): List<TrackEntity>

    /**
     * Проверяет, существует ли трек с указанным идентификатором в базе данных.
     *
     * @param trackName название трека, который нужно проверить.
     * @return `true`, если трек с таким идентификатором существует, и `false` в противном случае.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM $TRACK_TABLE_NAME WHERE $FIELD_TRACK_NAME = :trackName)")
    fun isTrackInDatabase(trackName: String): Boolean

    /**
     * Получает все треки из таблицы.
     *
     * @return Список всех треков в базе данных.
     */
    @Query("SELECT * FROM $TRACK_TABLE_NAME")
    fun getAllTracks(): List<TrackEntity>

    /**
     * Удаляет все треки из таблицы.
     */
    @Query("DELETE FROM $TRACK_TABLE_NAME")
    fun deleteAll()
}
