package com.example.spotify.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.spotify.data.converter.AlbumInfoTypeConverter
import com.example.spotify.data.converter.ArtistListTypeConverter
import com.example.spotify.domain.models.AlbumInfo

/**
 * Сущность трека для базы данных
 *
 * @property id идентификатор трека
 * @property name название трека
 * @property popularity популярность трека
 * @property previewUrl URL для предварительного прослушивания трека
 * @property duration продолжительность трека в секундах
 * @property artistsId список идентификаторов исполнителей трека
 * @property artistsName список имен исполнителей трека
 * @property album информация об альбоме, в который входит трек
 * @property isExplicit флаг, указывающий, содержит ли трек неприемлемый контент (нецензурные слова и т.п.)
 * @property isPlayable флаг, указывающий, можно ли воспроизвести трек
 */
@Entity(
    tableName = TRACK_TABLE_NAME
)
data class TrackEntity(
    @PrimaryKey
    @ColumnInfo(FIELD_TRACK_ID) val id: String,
    @ColumnInfo(FIELD_TRACK_NAME) val name: String,
    @ColumnInfo(FIELD_TRACK_POPULARITY) val popularity: Int,
    @ColumnInfo(FIELD_TRACK_URL) val previewUrl: String,
    @ColumnInfo(FIELD_TRACK_DURATION) val duration: Int,
    @ColumnInfo(FIELD_TRACK_ARTISTS_ID)
    @TypeConverters(ArtistListTypeConverter::class) val artistsId: List<String>,
    @ColumnInfo(FIELD_TRACK_ARTISTS_NAME)
    @TypeConverters(ArtistListTypeConverter::class) val artistsName: List<String>,
    @ColumnInfo(FIELD_TRACK_ALBUM)
    @TypeConverters(AlbumInfoTypeConverter::class) val album: AlbumInfo,
    @ColumnInfo(FIELD_TRACK_EXPLICIT) val isExplicit: Boolean,
    @ColumnInfo(FIELD_TRACK_PLAYABLE) val isPlayable: Boolean,
)

/**
 * Константа для имени поля "id" в таблице треков
 */
const val FIELD_TRACK_ID = "id"

/**
 * Константа для имени поля "name" в таблице треков
 */
const val FIELD_TRACK_NAME = "name"

/**
 * Константа для имени поля "popularity" в таблице треков
 */
const val FIELD_TRACK_POPULARITY = "popularity"

/**
 * Константа для имени поля "url" в таблице треков
 */
const val FIELD_TRACK_URL = "url"

/**
 * Константа для имени поля "duration" в таблице треков
 */
const val FIELD_TRACK_DURATION = "duration"

/**
 * Константа для имени поля "artists_id" в таблице треков
 */
const val FIELD_TRACK_ARTISTS_ID = "artists_id"

/**
 * Константа для имени поля "artists_name" в таблице треков
 */
const val FIELD_TRACK_ARTISTS_NAME = "artists_name"

/**
 * Константа для имени поля "album" в таблице треков
 */
const val FIELD_TRACK_ALBUM = "album"

/**
 * Константа для имени поля "isExplicit" в таблице треков
 */
const val FIELD_TRACK_EXPLICIT = "isExplicit"

/**
 * Константа для имени поля "isPlayable" в таблице треков
 */
const val FIELD_TRACK_PLAYABLE = "isPlayable"

/**
 * Константа для имени таблицы треков
 */
const val TRACK_TABLE_NAME = "track"
