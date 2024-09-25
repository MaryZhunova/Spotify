package com.example.spotify.data.storage

/**
 * Интерфейс для работы с файловым хранилищем
 */
interface FileStorage {

    /**
     * Сохраняет данные в файл с указанным именем.
     *
     * @param fileName Имя файла, в который будут сохранены данные.
     * @param data Данные, которые необходимо сохранить.
     * @param serializer Сериализатор, используемый для преобразования данных в байтовый массив.
     */
    fun <T> put(fileName: String, data: T, serializer: Serializer<T>)

    /**
     * Получает данные из файла с указанным именем.
     *
     * @param fileName Имя файла, из которого будут получены данные.
     * @param serializer Сериализатор, используемый для преобразования байтового массива в данные.
     * @return Десериализованные данные или `null`, если файл не существует.
     */
    fun <T> get(fileName: String, serializer: Serializer<T>): T?

    /**
     * Очищает хранилище, удаляя все данные.
     */
    fun clear()
}