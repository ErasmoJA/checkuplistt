package com.example.checkuplistt.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.checkuplistt.ui.theme.KawaiiColors

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaTrackerApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Watch List", "To-Do")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Watch List App", color = KawaiiColors.Ink) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KawaiiColors.Rose,  // barra pastel
                    titleContentColor = KawaiiColors.Ink,
                    navigationIconContentColor = KawaiiColors.Ink,
                    actionIconContentColor = KawaiiColors.Ink
                )
            )

        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(KawaiiColors.BgDark, KawaiiColors.BgLight)
                    )
                )
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = KawaiiColors.PixelCyan
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState > initialState) it else -it },
                        animationSpec = tween(300)
                    ) + fadeIn(tween(200)) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { if (targetState > initialState) -it else it },
                                animationSpec = tween(300)
                            ) + fadeOut(tween(200))
                },
                modifier = Modifier.fillMaxSize()
            ) { tab ->
                when (tab) {
                    0 -> WatchListScreen()
                    else -> TodoScreen()
                }
            }
        }
    }
}
