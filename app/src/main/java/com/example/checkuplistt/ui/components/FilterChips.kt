package com.example.checkuplistt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkuplistt.ui.theme.KawaiiColors

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
                    containerColor = KawaiiColors.BgLight,
                    selectedContainerColor = KawaiiColors.PixelCyan,
                    selectedLabelColor = KawaiiColors.BgDark,
                    labelColor = KawaiiColors.TextDim
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
                    containerColor = KawaiiColors.BgLight,
                    selectedContainerColor = KawaiiColors.PixelCyan,
                    selectedLabelColor = KawaiiColors.BgDark,
                    labelColor = KawaiiColors.TextDim
                )
            )

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
                    containerColor = KawaiiColors.BgLight,
                    selectedContainerColor = KawaiiColors.PixelCyan,
                    selectedLabelColor = KawaiiColors.BgDark,
                    labelColor = KawaiiColors.TextDim
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    containerColor = KawaiiColors.BgLight,
                    selectedContainerColor = KawaiiColors.PixelCyan,
                    selectedLabelColor = KawaiiColors.BgDark,
                    labelColor = KawaiiColors.TextDim
                )
            )

            FilterChip(
                onClick = { onFilterSelected("No vistas") },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🙈")
                        Text("No vistas", fontSize = 12.sp)
                    }
                },
                selected = selectedFilter == "No vistas",
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = KawaiiColors.BgLight,
                    selectedContainerColor = KawaiiColors.AccentMint, // suavecito
                    selectedLabelColor = KawaiiColors.Ink,
                    labelColor = KawaiiColors.TextMedium
                )

            )
        }
    }
}
