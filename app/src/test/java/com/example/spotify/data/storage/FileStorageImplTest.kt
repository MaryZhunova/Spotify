package com.example.spotify.data.storage

import android.content.Context
import com.example.spotify.utils.TimeSource
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import java.util.concurrent.TimeUnit

/**
 * Тесты [FileStorageImpl]
 */
class FileStorageImplTest {

    @TempDir
    private lateinit var tempDir: Path
    private lateinit var context: Context
    private lateinit var timeSource: TimeSource
    private lateinit var fileStorage: FileStorage
    private lateinit var serializer: Serializer<String>

    private lateinit var targetFolder: File

    @BeforeEach
    fun setUp() {
        targetFolder = tempDir.resolve("dir").toFile()
        context = mockk()
        timeSource = mockk()
        serializer = mockk()

        every { context.getExternalFilesDir(null) } returns tempDir.toFile()

        fileStorage = FileStorageImpl.Builder()
            .setDirectoryName(targetFolder.name)
            .setTimeout(2, TimeUnit.MINUTES)
            .build(context, timeSource)
    }

    @Test
    fun `put should write data to file`() {
        val testData = "testData"
        val timestamp = 1000L

        every { timeSource.getCurrentTime() } returns timestamp
        every { serializer.serialize(testData) } returns testData.toByteArray()

        fileStorage.put("testFile", testData, serializer)

        assertThat(targetFolder.listFiles()?.size).isEqualTo(1)
        assertThat(targetFolder.listFiles()?.get(0)?.name).isEqualTo("testFile.txt")
        assertThat(targetFolder.listFiles()?.get(0)?.readText()).isEqualTo("$timestamp|$testData")
    }

    @Test
    fun `get should return deserialized data from file`() {
        val timestamp = 1000L
        val testData = "testData"

        every { timeSource.getCurrentTime() } returns timestamp
        every { serializer.deserialize(testData.toByteArray()) } returns testData
        every { serializer.serialize(testData) } returns testData.toByteArray()

        fileStorage.put("testFile", testData, serializer)
        val result = fileStorage.get("testFile", serializer)

        assertThat(result).isEqualTo(testData)
    }

    @Test
    fun `get should return null if file does not exist`() {
        val result = fileStorage.get("nonExistentFile", serializer)

        assertThat(result).isNull()
    }

    @Test
    fun `clear should delete all files in directory`() {
        val timestamp = 1000L
        val testData = "testData"

        every { timeSource.getCurrentTime() } returns timestamp
        every { serializer.serialize(testData) } returns testData.toByteArray()

        fileStorage.put("testFile", testData, serializer)

        assertThat(targetFolder.exists()).isTrue()

        fileStorage.clear()

        assertThat(targetFolder.exists()).isFalse()
    }

    @Test
    fun `get should delete file if timeout exceeded`() {
        val timestamp = 1000L
        val testData = "testData"

        every { timeSource.getCurrentTime() } returns timestamp andThen timestamp + TimeUnit.MINUTES.toMillis(5)
        every { serializer.deserialize(testData.toByteArray()) } returns testData
        every { serializer.serialize(testData) } returns testData.toByteArray()

        fileStorage.put("testFile", testData, serializer)
        val result = fileStorage.get("testFile", serializer)

        assertThat(result).isNull()
    }
}