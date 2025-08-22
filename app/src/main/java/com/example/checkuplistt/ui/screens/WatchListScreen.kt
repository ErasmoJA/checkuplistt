package com.example.checkuplistt.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.checkuplistt.data.loadMediaList
import com.example.checkuplistt.data.saveMediaList
import com.example.checkuplistt.model.MediaType
import com.example.checkuplistt.model.WatchStatus
import com.example.checkuplistt.ui.components.FilterChips
import com.example.checkuplistt.ui.components.MediaItemCard
import com.example.checkuplistt.ui.components.StatsCards
import com.example.checkuplistt.ui.dialog.AddMediaDialog
import com.example.checkuplistt.ui.theme.KawaiiColors

@Composable
fun WatchListScreen() {
    val context = LocalContext.current
    var mediaList by remember { mutableStateOf(loadMediaList(context)) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todas") }

    LaunchedEffect(mediaList) { saveMediaList(context, mediaList) }

    val filteredList = when (selectedFilter) {
        "Películas" -> mediaList.filter { it.type == MediaType.MOVIE }
        "Series"    -> mediaList.filter { it.type == MediaType.SERIES }
        "Vistas"    -> mediaList.filter { it.status == WatchStatus.WATCHED }
        "No vistas" -> mediaList.filter { it.status == WatchStatus.TO_WATCH }
        else        -> mediaList
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(Modifier.height(12.dp))
            StatsCards(mediaList)
            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredList, key = { it.id }) { item ->
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
                item { Spacer(Modifier.height(80.dp)) }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = KawaiiColors.AccentLilac, // o AccentRose si prefieres
            contentColor   = KawaiiColors.Ink,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar")
        }
    }

    if (showAddDialog) {
        AddMediaDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newItem ->
                mediaList = mediaList + newItem.copy(
                    id = mediaList.maxOfOrNull { it.id }?.plus(1) ?: 1
                )
                showAddDialog = false
            }
        )
    }
}
