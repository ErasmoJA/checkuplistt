package com.example.checkuplistt.model

data class MediaItem(
    val id: Int,
    val title: String,
    val type: MediaType,
    val status: WatchStatus,
    val rating: Int = 0,
    val year: Int = 0,
    val genre: String = ""
)

enum class MediaType { MOVIE, SERIES }
enum class WatchStatus { WATCHED, TO_WATCH }
