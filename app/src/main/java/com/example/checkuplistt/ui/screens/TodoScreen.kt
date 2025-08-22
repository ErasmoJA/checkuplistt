package com.example.checkuplistt.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.checkuplistt.data.loadTasks
import com.example.checkuplistt.data.saveTasks
import com.example.checkuplistt.model.TaskItem
import com.example.checkuplistt.ui.theme.KawaiiColors

@Composable
fun TodoScreen() {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var tasks by remember { mutableStateOf(loadTasks(ctx)) }
    var newTask by remember { mutableStateOf("") }

    LaunchedEffect(tasks) { saveTasks(ctx, tasks) }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Row {
                OutlinedTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = { Text("Nueva actividad") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newTask.isNotBlank()) {
                            val id = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                            tasks = tasks + TaskItem(id, newTask.trim(), false)
                            newTask = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KawaiiColors.PixelCyan, contentColor = KawaiiColors.BgDark)
                ) { Icon(Icons.Default.Add, contentDescription = null) }
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = {
                            tasks = tasks.map { if (it.id == task.id) it.copy(done = !it.done) else it }
                        },
                        onDelete = {
                            tasks = tasks.filter { it.id != task.id }
                        }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun TaskRow(task: TaskItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(checked = task.done, onCheckedChange = { onToggle() })
                Spacer(Modifier.width(8.dp))
                Text(
                    text = task.title,
                    textDecoration = if (task.done) TextDecoration.LineThrough else null
                )
            }
            AnimatedVisibility(visible = task.done, enter = fadeIn(), exit = fadeOut()) {
                Text("✔", color = KawaiiColors.PixelCyan)
            }
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = KawaiiColors.PixelRed)) {
                Text("Eliminar")
            }
        }
    }
}
