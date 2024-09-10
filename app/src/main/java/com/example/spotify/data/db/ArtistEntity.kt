package com.example.spotify.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность таблицы исполнителей в базе данных
 *
 * @property id идентификатор исполнителя
 * @property name название исполнителя
 * @property popularity популярность исполнителя
 * @property genres жанры, к которым относится исполнитель, в виде строки
 * @property smallImage URL изображения маленького размера, связанного с исполнителем
 * @property bigImage URL изображения большого размера, связанного с исполнителем
 */
@Entity(
    tableName = ARTIST_TABLE_NAME
)
data class ArtistEntity(
    @PrimaryKey
    @ColumnInfo(FIELD_ARTIST_ID) val id: String,
    @ColumnInfo(FIELD_ARTIST_NAME) val name: String,
    @ColumnInfo(FIELD_ARTIST_POPULARITY) val popularity: Int,
    @ColumnInfo(FIELD_ARTIST_GENRES) val genres: String,
    @ColumnInfo(FIELD_ARTIST_SMALL_IMAGE) val smallImage: String,
    @ColumnInfo(FIELD_ARTIST_BIG_IMAGE) val bigImage: String,
)

/**
 * Константа для названия поля "id" в таблице исполнителей
 */
const val FIELD_ARTIST_ID = "id"

/**
 * Константа для названия поля "name" в таблице исполнителей
 */
const val FIELD_ARTIST_NAME = "name"

/**
 * Константа для названия поля "popularity" в таблице исполнителей
 */
const val FIELD_ARTIST_POPULARITY = "popularity"

/**
 * Константа для названия поля "genres" в таблице исполнителей
 */
const val FIELD_ARTIST_GENRES = "genres"

/**
 * Константа для названия поля "small_image" в таблице исполнителей
 */
const val FIELD_ARTIST_SMALL_IMAGE = "small_image"

/**
 * Константа для названия поля "big_image" в таблице исполнителей
 */
const val FIELD_ARTIST_BIG_IMAGE = "big_image"

/**
 * Константа для названия таблицы исполнителей
 */
const val ARTIST_TABLE_NAME = "artist"
