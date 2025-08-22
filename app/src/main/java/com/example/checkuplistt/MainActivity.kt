package com.example.checkuplistt

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkuplistt.ui.theme.CheckuplisttTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class MediaItem(
    val id: Int,
    val title: String,
    val type: MediaType,
    val status: WatchStatus,
    val rating: Int = 0,
    val year: Int = 0,
    val genre: String = ""
)

enum class MediaType {
    MOVIE, SERIES
}

enum class WatchStatus {
    WATCHED, TO_WATCH
}

// Funciones para guardar y cargar datos
fun saveMediaList(context: Context, mediaList: List<MediaItem>) {
    try {
        val sharedPreferences = context.getSharedPreferences("media_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(mediaList)
        sharedPreferences.edit().putString("media_list", json).apply()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadMediaList(context: Context): List<MediaItem> {
    return try {
        val sharedPreferences = context.getSharedPreferences("media_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("media_list", null)
        if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<MediaItem>>() {}.type
            gson.fromJson(json, type) ?: getSampleData()
        } else {
            getSampleData()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        getSampleData()
    }
}

object KawaiiColors {
    val PinkSoft = Color(0xFFFDE2E7)
    val PurpleSoft = Color(0xFFF3E8FF)
    val BlueSoft = Color(0xFFEBF4FF)
    val GreenSoft = Color(0xFFECFDF5)
    val YellowSoft = Color(0xFFFEFCE8)
    val WhiteCream = Color(0xFFFFFBF7)
    val GrayLight = Color(0xFFF1F5F9)
    val TextDark = Color(0xFF1F2937)
    val TextMedium = Color(0xFF4B5563)
    val TextLight = Color(0xFF6B7280)
    val PinkBright = Color(0xFFEC4899)
    val PurpleBright = Color(0xFF8B5CF6)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckuplisttTheme {
                MediaTrackerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaTrackerApp() {
    val context = LocalContext.current
    var mediaList by remember { mutableStateOf(loadMediaList(context)) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todas") }

    // Guardar datos cada vez que la lista cambie
    LaunchedEffect(mediaList) {
        saveMediaList(context, mediaList)
    }

    val filteredList = when (selectedFilter) {
        "Películas" -> mediaList.filter { it.type == MediaType.MOVIE }
        "Series" -> mediaList.filter { it.type == MediaType.SERIES }
        "Vistas" -> mediaList.filter { it.status == WatchStatus.WATCHED }
        "Por Ver" -> mediaList.filter { it.status == WatchStatus.TO_WATCH }
        else -> mediaList
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8E8F5),
                        Color(0xFFF0E8FF),
                        Color(0xFFF5F0FF)
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🐱", fontSize = 24.sp)
                            Text(
                                "Mi Lista",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B46C1)
                            )
                            Text("🎬", fontSize = 20.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFF8E8F5).copy(alpha = 0.95f)
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color(0xFF8B5CF6),
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Filtros
                FilterChips(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Estadísticas
                StatsCards(mediaList)

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de contenido
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { item ->
                        MediaItemCard(
                            item = item,
                            onStatusChange = { newStatus ->
                                mediaList = mediaList.map {
                                    if (it.id == item.id) it.copy(status = newStatus) else it
                                }
                            },
                            onRatingChange = { newRating ->
                                mediaList = mediaList.map {
                                    if (it.id == item.id) it.copy(rating = newRating) else it
                                }
                            },
                            onDelete = {
                                mediaList = mediaList.filter { it.id != item.id }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMediaDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newItem ->
                mediaList = mediaList + newItem.copy(id = mediaList.maxOfOrNull { it.id }?.plus(1) ?: 1)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun FilterChips(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                onClick = { onFilterSelected("Todas") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🌈")
                        Text("Todas", fontSize = 12.sp)
                    }
                },
                selected = selectedFilter == "Todas",
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = KawaiiColors.WhiteCream,
                    selectedContainerColor = KawaiiColors.PurpleBright,
                    selectedLabelColor = Color.White,
                    labelColor = KawaiiColors.TextMedium
                )
            )

            FilterChip(
                onClick = { onFilterSelected("Películas") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🎬")
                        Text("Películas", fontSize = 12.sp)
                    }
                },
                selected = selectedFilter == "Películas",
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = KawaiiColors.WhiteCream,
                    selectedContainerColor = KawaiiColors.PurpleBright,
                    selectedLabelColor = Color.White,
                    labelColor = KawaiiColors.TextMedium
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                onClick = { onFilterSelected("Series") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("📺")
                        Text("Series", fontSize = 12.sp)
                    }
                },
                selected = selectedFilter == "Series",
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = KawaiiColors.WhiteCream,
                    selectedContainerColor = KawaiiColors.PurpleBright,
                    selectedLabelColor = Color.White,
                    labelColor = KawaiiColors.TextMedium
                )
            )

            FilterChip(
                onClick = { onFilterSelected("Vistas") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("😸")
                        Text("Vistas", fontSize = 12.sp)
                    }
                },
                selected = selectedFilter == "Vistas",
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = KawaiiColors.WhiteCream,
                    selectedContainerColor = Color(0xFF10B981),
                    selectedLabelColor = Color.White,
                    labelColor = KawaiiColors.TextMedium
                )
            )
        }
    }
}

@Composable
fun StatsCards(mediaList: List<MediaItem>) {
    val totalMovies = mediaList.count { it.type == MediaType.MOVIE }
    val totalSeries = mediaList.count { it.type == MediaType.SERIES }
    val watchedCount = mediaList.count { it.status == WatchStatus.WATCHED }
    val toWatchCount = mediaList.count { it.status == WatchStatus.TO_WATCH }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            title = "Películas",
            value = totalMovies.toString(),
            emoji = "🎬",
            color = KawaiiColors.PinkSoft,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Series",
            value = totalSeries.toString(),
            emoji = "📺",
            color = KawaiiColors.PurpleSoft,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Vistas",
            value = watchedCount.toString(),
            emoji = "😸",
            color = KawaiiColors.GreenSoft,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Por Ver",
            value = toWatchCount.toString(),
            emoji = "😺",
            color = KawaiiColors.YellowSoft,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = KawaiiColors.TextDark
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = KawaiiColors.TextMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (item.type == MediaType.MOVIE) "🎬" else "📺",
                            fontSize = 20.sp
                        )
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
                        Spacer(modifier = Modifier.height(4.dp))
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
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = KawaiiColors.TextMedium
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(if (item.status == WatchStatus.WATCHED) "❌" else "✅")
                                Text(
                                    if (item.status == WatchStatus.WATCHED) "Marcar como no vista" else "Marcar como vista"
                                )
                            }
                        },
                        onClick = {
                            onStatusChange(
                                if (item.status == WatchStatus.WATCHED)
                                    WatchStatus.TO_WATCH
                                else
                                    WatchStatus.WATCHED
                            )
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🗑️")
                                Text("Eliminar")
                            }
                        },
                        onClick = {
                            onDelete()
                            showMenu = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onStatusChange(
                                if (item.status == WatchStatus.WATCHED)
                                    WatchStatus.TO_WATCH
                                else
                                    WatchStatus.WATCHED
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (item.status == WatchStatus.WATCHED)
                                Color(0xFF10B981) else KawaiiColors.PinkBright
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (item.status == WatchStatus.WATCHED) "😸 Vista" else "😺 Marcar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = { onDelete() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("🗑️", fontSize = 14.sp)
                    }
                }

                if (item.status == WatchStatus.WATCHED) {
                    KittyRating(
                        rating = item.rating,
                        onRatingChange = onRatingChange
                    )
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
            .background(
                Color.White,
                RoundedCornerShape(16.dp)
            )
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🐱")
                Text("Agregar contenido")
                Text("🎬")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("📝 Título") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Tipo de contenido
                Card(
                    colors = CardDefaults.cardColors(containerColor = KawaiiColors.GrayLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("🎭 Tipo:", fontWeight = FontWeight.Medium)

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedType == MediaType.MOVIE,
                                    onClick = { selectedType = MediaType.MOVIE }
                                )
                                Text("🎬 Película")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedType == MediaType.SERIES,
                                    onClick = { selectedType = MediaType.SERIES }
                                )
                                Text("📺 Serie")
                            }
                        }
                    }
                }

                // Estado
                Card(
                    colors = CardDefaults.cardColors(containerColor = KawaiiColors.GrayLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("😸 Estado:", fontWeight = FontWeight.Medium)

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedStatus == WatchStatus.TO_WATCH,
                                    onClick = {
                                        selectedStatus = WatchStatus.TO_WATCH
                                        rating = 0
                                    }
                                )
                                Text("😺 Por ver")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedStatus == WatchStatus.WATCHED,
                                    onClick = { selectedStatus = WatchStatus.WATCHED }
                                )
                                Text("😸 Vista")
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 4) year = it },
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
                    Card(
                        colors = CardDefaults.cardColors(containerColor = KawaiiColors.GrayLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("😻 Calificación:", fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(8.dp))
                            KittyRating(
                                rating = rating,
                                onRatingChange = { rating = it }
                            )
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
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

// Datos de ejemplo
fun getSampleData(): List<MediaItem> {
    return listOf(
        MediaItem(
            id = 1,
            title = "El Padrino",
            type = MediaType.MOVIE,
            status = WatchStatus.WATCHED,
            rating = 5,
            year = 1972,
            genre = "Drama"
        ),
        MediaItem(
            id = 2,
            title = "Breaking Bad",
            type = MediaType.SERIES,
            status = WatchStatus.WATCHED,
            rating = 5,
            year = 2008,
            genre = "Drama"
        ),
        MediaItem(
            id = 3,
            title = "The Office",
            type = MediaType.SERIES,
            status = WatchStatus.TO_WATCH,
            year = 2005,
            genre = "Comedia"
        )
    )
}