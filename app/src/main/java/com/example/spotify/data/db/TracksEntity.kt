package com.example.spotify.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.spotify.data.converter.AlbumInfoTypeConverter
import com.example.spotify.data.converter.ArtistListTypeConverter
import com.example.spotify.models.data.AlbumInfo

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


const val FIELD_TRACK_ID = "id"
const val FIELD_TRACK_NAME = "name"
const val FIELD_TRACK_POPULARITY = "popularity"
const val FIELD_TRACK_URL = "url"
const val FIELD_TRACK_DURATION = "duration"
const val FIELD_TRACK_ARTISTS_ID = "artists_id"
const val FIELD_TRACK_ARTISTS_NAME = "artists_name"
const val FIELD_TRACK_ALBUM = "album"
const val FIELD_TRACK_EXPLICIT = "isExplicit"
const val FIELD_TRACK_PLAYABLE = "isPlayable"
const val TRACK_TABLE_NAME = "track"