package com.example.checkuplistt.model

data class TaskItem(
    val id: Int,
    val title: String,
    val done: Boolean = false
)
