package com.example.checkuplistt.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkuplistt.model.MediaItem
import com.example.checkuplistt.model.MediaType
import com.example.checkuplistt.model.WatchStatus
import com.example.checkuplistt.ui.components.KittyRating
import com.example.checkuplistt.ui.theme.KawaiiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMediaDialog(
    onDismiss: () -> Unit,
    onAdd: (MediaItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MediaType.MOVIE) }
    var selectedStatus by remember { mutableStateOf(WatchStatus.TO_WATCH) }
    var year by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🐱"); Text("Agregar contenido"); Text("🎬")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("📝 Título") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Tipo
                Card(colors = CardDefaults.cardColors(containerColor = KawaiiColors.GrayLight), shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("🎭 Tipo:", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedType == MediaType.MOVIE, onClick = { selectedType = MediaType.MOVIE })
                                Text("🎬 Película")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedType == MediaType.SERIES, onClick = { selectedType = MediaType.SERIES })
                                Text("📺 Serie")
                            }
                        }
                    }
                }

                // Estado
                Card(colors = CardDefaults.cardColors(containerColor = KawaiiColors.GrayLight), shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("😸 Estado:", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedStatus == WatchStatus.TO_WATCH,
                                    onClick = { selectedStatus = WatchStatus.TO_WATCH; rating = 0 }
                                )
                                Text("😺 Por ver")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedStatus == WatchStatus.WATCHED, onClick = { selectedStatus = WatchStatus.WATCHED })
                                Text("😸 Vista")
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 4) year = it },
                        label = { Text("📅 Año") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text("🎨 Género") },
                        modifier = Modifier.weight(2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                if (selectedStatus == WatchStatus.WATCHED) {
                    Card(colors = CardDefaults.cardColors(containerColor = KawaiiColors.GrayLight), shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("😻 Calificación:", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                            Spacer(Modifier.height(8.dp))
                            KittyRating(rating = rating, onRatingChange = { rating = it })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(
                            MediaItem(
                                id = 0,
                                title = title.trim(),
                                type = selectedType,
                                status = selectedStatus,
                                rating = if (selectedStatus == WatchStatus.WATCHED) rating else 0,
                                year = year.toIntOrNull() ?: 0,
                                genre = genre.trim()
                            )
                        )
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = KawaiiColors.PurpleBright),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        containerColor = androidx.compose.ui.graphics.Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
