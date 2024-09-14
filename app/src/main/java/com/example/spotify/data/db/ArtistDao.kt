package com.example.spotify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spotify.data.models.db.ARTIST_TABLE_NAME
import com.example.spotify.data.models.db.ArtistEntity
import com.example.spotify.data.models.db.FIELD_ARTIST_ID

/**
 * DAO (Data Access Object) для работы с таблицей исполнителей в базе данных
 */
@Dao
interface ArtistDao {

    /**
     * Получает все записи из таблицы исполнителей.
     *
     * @return Список всех исполнителей в базе данных
     */
    @Query("SELECT * FROM $ARTIST_TABLE_NAME")
    fun getAll(): List<ArtistEntity>

    /**
     * Получает исполнителей по их идентификаторам.
     *
     * @param artistsIds Перечисление идентификаторов исполнителей, которые нужно получить.
     * @return Список исполнителей, соответствующих переданным идентификаторам.
     */
    @Query("SELECT * FROM $ARTIST_TABLE_NAME WHERE $FIELD_ARTIST_ID IN (:artistsIds)")
    fun getByIds(vararg artistsIds: String): List<ArtistEntity>

    /**
     * Получает исполнителя по его идентификатору.
     *
     * @param artistId Идентификатор исполнителя, которого нужно получить.
     * @return Исполнитель, соответствующий переданному идентификатору.
     */
    @Query("SELECT * FROM $ARTIST_TABLE_NAME WHERE $FIELD_ARTIST_ID = :artistId")
    fun getById(artistId: String): ArtistEntity

    /**
     * Вставляет записи исполнителей в таблицу. Если запись с таким же идентификатором уже существует, то она будет проигнорирована.
     *
     * @param users Перечисление исполнителей, которые нужно вставить в таблицу.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: ArtistEntity)

    /**
     * Удаляет все записи из таблицы исполнителей.
     */
    @Query("DELETE FROM $ARTIST_TABLE_NAME")
    fun deleteAll()
}