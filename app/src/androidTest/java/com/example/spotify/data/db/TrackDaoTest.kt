package com.example.spotify.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotify.models.data.AlbumInfo
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тесты [TrackDao]
 */
@RunWith(AndroidJUnit4::class)
class TrackDaoTest {

    private lateinit var db: SpotifyDatabase
    private lateinit var trackDao: TrackDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SpotifyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        trackDao = db.trackDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetAllTracks() {

        trackDao.insertAll(track1, track2)
        val result = trackDao.getAllTracks()

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(track1, track2)
    }

    @Test
    fun findTracksByArtistId() {

        trackDao.insertAll(track1)
        val result = trackDao.findTracksByArtistId("artist1")

        assertThat(result).containsExactly(track1)
    }

    @Test
    fun isTrackInDatabase() {

        trackDao.insertAll(track1)
        val exists = trackDao.isTrackInDatabase("Track 1")
        val notExists = trackDao.isTrackInDatabase("Track 2")

        assertThat(exists).isTrue()
        assertThat(notExists).isFalse()
    }

    @Test
    fun deleteAllTracks() {

        trackDao.insertAll(track1, track2)
        trackDao.deleteAll()
        val result = trackDao.getAllTracks()

        assertThat(result).isEmpty()
    }

    private companion object {
        val track1 = TrackEntity(
            id = "1",
            name = "Track 1",
            popularity = 85,
            previewUrl = "http://example.com/preview1.mp3",
            duration = 200,
            artistsId = listOf("artist1"),
            artistsName = listOf("Artist 1"),
            album = AlbumInfo(id = "artist1", name = "Album 1", image = "http://example.com/album1.jpg"),
            isExplicit = false,
            isPlayable = true
        )
        val track2 = TrackEntity(
            id = "2",
            name = "Track 2",
            popularity = 90,
            previewUrl = "http://example.com/preview2.mp3",
            duration = 180,
            artistsId = listOf("artist2"),
            artistsName = listOf("Artist 2"),
            album = AlbumInfo(id = "artist2", name = "Album 2", image = "http://example.com/album2.jpg"),
            isExplicit = true,
            isPlayable = true
        )

    }
}
