package com.example.checkuplistt.data

import android.content.Context
import com.example.checkuplistt.model.MediaItem
import com.example.checkuplistt.model.MediaType
import com.example.checkuplistt.model.WatchStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PREFS_NAME = "media_prefs"
private const val KEY_MEDIA_LIST = "media_list"

fun saveMediaList(context: Context, mediaList: List<MediaItem>) {
    try {
        val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sp.edit().putString(KEY_MEDIA_LIST, Gson().toJson(mediaList)).apply()
    } catch (e: Exception) { e.printStackTrace() }
}

fun loadMediaList(context: Context): List<MediaItem> = try {
    val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val json = sp.getString(KEY_MEDIA_LIST, null)
    if (json != null) {
        val type = object : TypeToken<List<MediaItem>>() {}.type
        Gson().fromJson<List<MediaItem>>(json, type) ?: getSampleData()
    } else getSampleData()
} catch (e: Exception) {
    e.printStackTrace(); getSampleData()
}

fun getSampleData(): List<MediaItem> = listOf(
    MediaItem(1, "El Padrino", MediaType.MOVIE, WatchStatus.WATCHED, 5, 1972, "Drama"),
    MediaItem(2, "Breaking Bad", MediaType.SERIES, WatchStatus.WATCHED, 5, 2008, "Drama"),
    MediaItem(3, "The Office", MediaType.SERIES, WatchStatus.TO_WATCH, 0, 2005, "Comedia")
)
