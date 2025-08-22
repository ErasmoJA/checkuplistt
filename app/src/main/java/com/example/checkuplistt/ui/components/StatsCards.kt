package com.example.checkuplistt.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkuplistt.model.MediaItem
import com.example.checkuplistt.model.MediaType
import com.example.checkuplistt.model.WatchStatus
import com.example.checkuplistt.ui.theme.KawaiiColors

@Composable
fun StatsCards(mediaList: List<MediaItem>) {
    val totalMovies = mediaList.count { it.type == MediaType.MOVIE }
    val totalSeries = mediaList.count { it.type == MediaType.SERIES }
    val watchedCount = mediaList.count { it.status == WatchStatus.WATCHED }
    val toWatchCount = mediaList.count { it.status == WatchStatus.TO_WATCH }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatCard("Películas", totalMovies.toString(), "🎬", KawaiiColors.PinkSoft, Modifier.weight(1f))
        StatCard("Series", totalSeries.toString(), "📺", KawaiiColors.PurpleSoft, Modifier.weight(1f))
        StatCard("Vistas", watchedCount.toString(), "😸", KawaiiColors.GreenSoft, Modifier.weight(1f))
        StatCard("Por Ver", toWatchCount.toString(), "😺", KawaiiColors.YellowSoft, Modifier.weight(1f))
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    emoji: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = KawaiiColors.TextDark)
            Text(title, fontSize = 10.sp, color = KawaiiColors.TextMedium, fontWeight = FontWeight.Medium)
        }
    }
}
