package com.example.spotify.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotify.data.models.db.ArtistEntity
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тесты [ArtistDao]
 */
@RunWith(AndroidJUnit4::class)
class ArtistDaoTest {

    private lateinit var db: SpotifyDatabase
    private lateinit var artistDao: ArtistDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SpotifyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        artistDao = db.artistDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetAllArtists() {
        artistDao.insertAll(artist1, artist2)
        val result = artistDao.getAll()

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(artist1, artist2)
    }

    @Test
    fun getArtistById() {
        artistDao.insertAll(artist1)
        val result = artistDao.getById("1")

        assertThat(result).isEqualTo(artist1)
    }

    @Test
    fun getArtistsByIds() {
        artistDao.insertAll(artist1, artist2)
        val result = artistDao.getByIds("1", "2")

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(artist1, artist2)
    }

    @Test
    fun insertAndIgnoreDuplicateArtists() {
        val duplicateArtist = ArtistEntity(
            id = "1",
            name = "Duplicate Artist 1",
            popularity = 90,
            genres = "Jazz, Blues",
            smallImage = "http://example.com/small2.jpg",
            bigImage = "http://example.com/big2.jpg"
        )

        artistDao.insertAll(artist1)
        artistDao.insertAll(duplicateArtist)
        val result = artistDao.getAll()

        assertThat(result).hasSize(1)
        assertThat(result.first().name).isEqualTo("Artist 1")
    }

    @Test
    fun deleteAllArtists() {
        artistDao.insertAll(artist1, artist2)

        artistDao.deleteAll()
        val result = artistDao.getAll()

        assertThat(result).isEmpty()
    }
    private companion object {

        val artist1 = ArtistEntity(
            id = "1",
            name = "Artist 1",
            popularity = 85,
            genres = "Pop, Rock",
            smallImage = "http://example.com/small1.jpg",
            bigImage = "http://example.com/big1.jpg"
        )
        val artist2 = ArtistEntity(
            id = "2",
            name = "Artist 2",
            popularity = 90,
            genres = "Jazz, Blues",
            smallImage = "http://example.com/small2.jpg",
            bigImage = "http://example.com/big2.jpg"
        )
    }
}
