package com.example.spotify.data.storage

import android.content.Context
import com.example.spotify.utils.TimeSource
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Реализация интерфейса [FileStorage].
 *
 * @param context Контекст приложения, используемый для работы с файловой системой.
 * @param timeSource Источник времени, используемый для получения текущего времени.
 */
class FileStorageImpl @Inject constructor(
    private val context: Context,
    private val timeSource: TimeSource
): FileStorage {

    private var directoryName: String? = null
    private var timeout: Long = Long.MAX_VALUE

    override fun <T> put(fileName: String, data: T, serializer: Serializer<T>) {
        val dir = getDirectory() ?: return
        val file = File(dir, "$fileName.txt")
        val timestamp = timeSource.getCurrentTime()
        val dataWithTimestamp = "${timestamp}|${String(serializer.serialize(data))}"

        FileOutputStream(file).use { outputStream ->
            outputStream.write(dataWithTimestamp.toByteArray())
        }
    }

    override fun <T> get(fileName: String, serializer: Serializer<T>): T? {
        val dir = getDirectory() ?: return null
        val file = File(dir, "$fileName.txt")
        if (!file.exists()) return null

        val content = file.readBytes().toString(Charsets.UTF_8)
        val parts = content.split("|")
        if (parts.size != 2) return null

        val timestamp = parts[0].toLong()
        val dataBytes = parts[1].toByteArray()

        // Check if the data is still valid
        if (timeSource.getCurrentTime() - timestamp > timeout) {
            file.delete()
            return null
        }

        return serializer.deserialize(dataBytes)
    }

    override fun clear() {
        val dir = getDirectory() ?: return
        dir.deleteRecursively()
    }

    private fun getDirectory(): File? {
        return directoryName?.let {
            File(context.getExternalFilesDir(null), it).apply { mkdirs() }
        }
    }

    /**
     * Билдер для создания экземпляра [FileStorage].
     *
     * Позволяет настраивать параметры перед созданием объекта.
     */
    class Builder {
        private var directoryName: String? = null
        private var timeout: Long = Long.MAX_VALUE

        /**
         * Устанавливает имя директории для хранения файлов.
         *
         * @param name Имя директории.
         * @return Текущий экземпляр билдера для цепочки вызовов.
         */
        fun setDirectoryName(name: String) = apply { this.directoryName = name }

        /**
         * Устанавливает тайм-аут для хранения данных.
         *
         * @param time тайм-аут
         * @param unit Единица измерения времени.
         * @return Текущий экземпляр билдера для цепочки вызовов.
         */
        fun setTimeout(time: Long, unit: TimeUnit) = apply { this.timeout = unit.toMillis(time) }

        /**
         * Создает экземпляр [FileStorage] с заданными параметрами.
         *
         * @param context Контекст приложения, используемый для работы с файловой системой.
         * @param timeSource Источник времени, используемый для получения текущего времени.
         * @return Экземпляр [FileStorage].
         */
        fun build(context: Context, timeSource: TimeSource): FileStorage {
            val fileStorage = FileStorageImpl(context, timeSource)
            fileStorage.directoryName = directoryName
            fileStorage.timeout = timeout
            return fileStorage
        }
    }
}