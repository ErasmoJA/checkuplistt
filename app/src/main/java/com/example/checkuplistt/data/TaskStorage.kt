package com.example.checkuplistt.data

import android.content.Context
import com.example.checkuplistt.model.TaskItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val PREFS_NAME = "todo_prefs"
private const val KEY_TASKS = "tasks"

fun loadTasks(context: Context): List<TaskItem> {
    val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val json = sp.getString(KEY_TASKS, null) ?: return emptyList()
    val type = object : TypeToken<List<TaskItem>>() {}.type
    return Gson().fromJson(json, type) ?: emptyList()
}

fun saveTasks(context: Context, tasks: List<TaskItem>) {
    val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    sp.edit().putString(KEY_TASKS, Gson().toJson(tasks)).apply()
}
