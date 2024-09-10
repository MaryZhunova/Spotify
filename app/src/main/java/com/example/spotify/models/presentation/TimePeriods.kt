package com.example.spotify.models.presentation

/**
 * Enum для представления различных временных периодов
 *
 * @property strValue Строковое значение периода, используемое для идентификации периода в запросах.
 * @property nameValue Отображаемое имя периода, которое используется для отображения пользователю.
 */
enum class TimePeriods(val strValue: String, val nameValue: String) {

    SHORT("short_term", "4 weeks"),

    MEDIUM("medium_term", "6 months"),

    LONG("long_term", "12 months")
}