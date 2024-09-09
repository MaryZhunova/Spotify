package com.example.spotify.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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


const val FIELD_ARTIST_ID = "id"
const val FIELD_ARTIST_NAME = "name"
const val FIELD_ARTIST_POPULARITY = "popularity"
const val FIELD_ARTIST_GENRES = "genres"
const val FIELD_ARTIST_SMALL_IMAGE = "small_image"
const val FIELD_ARTIST_BIG_IMAGE = "big_image"
const val ARTIST_TABLE_NAME = "artist"