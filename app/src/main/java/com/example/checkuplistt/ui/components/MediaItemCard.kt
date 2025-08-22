package com.example.checkuplistt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkuplistt.model.MediaItem
import com.example.checkuplistt.model.MediaType
import com.example.checkuplistt.model.WatchStatus
import com.example.checkuplistt.ui.theme.KawaiiColors

@Composable
fun MediaItemCard(
    item: MediaItem,
    onStatusChange: (WatchStatus) -> Unit,
    onRatingChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val cardColor = when {
        item.status == WatchStatus.WATCHED && item.rating >= 4 -> KawaiiColors.GreenSoft
        item.status == WatchStatus.WATCHED -> KawaiiColors.BlueSoft
        else -> KawaiiColors.PinkSoft
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (item.type == MediaType.MOVIE) "🎬" else "📺", fontSize = 20.sp)
                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = KawaiiColors.TextDark
                        )
                    }

                    if (item.year > 0 || item.genre.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = buildString {
                                if (item.year > 0) append("📅 ${item.year}")
                                if (item.genre.isNotEmpty()) {
                                    if (item.year > 0) append(" • ")
                                    append("🎨 ${item.genre}")
                                }
                            },
                            fontSize = 12.sp,
                            color = KawaiiColors.TextLight
                        )
                    }
                }

                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Más opciones", tint = KawaiiColors.TextMedium)
                }

                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(if (item.status == WatchStatus.WATCHED) "❌" else "✅")
                                Text(if (item.status == WatchStatus.WATCHED) "Marcar como no vista" else "Marcar como vista")
                            }
                        },
                        onClick = {
                            onStatusChange(if (item.status == WatchStatus.WATCHED) WatchStatus.TO_WATCH else WatchStatus.WATCHED)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("🗑️"); Text("Eliminar")
                            }
                        },
                        onClick = { onDelete(); showMenu = false }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            onStatusChange(if (item.status == WatchStatus.WATCHED) WatchStatus.TO_WATCH else WatchStatus.WATCHED)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (item.status == WatchStatus.WATCHED)
                                androidx.compose.ui.graphics.Color(0xFF10B981) else KawaiiColors.PinkBright
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(if (item.status == WatchStatus.WATCHED) "😸 Vista" else "😺 Marcar", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFEF4444)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("🗑️", fontSize = 14.sp) }
                }

                if (item.status == WatchStatus.WATCHED) {
                    KittyRating(rating = item.rating, onRatingChange = onRatingChange)
                }
            }
        }
    }
}

@Composable
fun KittyRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    maxStars: Int = 5
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        for (i in 1..maxStars) {
            Text(
                text = if (i <= rating) "😻" else "😿",
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { onRatingChange(i) }
                    .padding(2.dp)
            )
        }
    }
}
