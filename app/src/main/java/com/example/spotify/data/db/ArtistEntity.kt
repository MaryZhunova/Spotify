package com.example.spotify.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = ARTIST_TABLE_NAME
)
data class ArtistEntity(
    @PrimaryKey
    @ColumnInfo(FIELD_ID) val id: String,
    @ColumnInfo(FIELD_NAME) val name: String,
    @ColumnInfo(FIELD_POPULARITY) val popularity: Int,
    @ColumnInfo(FIELD_GENRES) val genres: String,
    @ColumnInfo(FIELD_SMALL_IMAGE) val smallImage: String,
    @ColumnInfo(FIELD_BIG_IMAGE) val bigImage: String,
)


const val FIELD_ID = "id"
const val FIELD_NAME = "name"
const val FIELD_POPULARITY = "popularity"
const val FIELD_GENRES = "genres"
const val FIELD_SMALL_IMAGE = "small_image"
const val FIELD_BIG_IMAGE = "big_image"
const val ARTIST_TABLE_NAME = "artist"