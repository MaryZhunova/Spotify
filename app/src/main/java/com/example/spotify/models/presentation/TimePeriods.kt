package com.example.spotify.models.presentation

enum class TimePeriods(val strValue: String, val nameValue: String) {

    SHORT("short_term", "4 weeks"),

    MEDIUM("medium_term", "6 months"),

    LONG("long_term", "12 months")
}