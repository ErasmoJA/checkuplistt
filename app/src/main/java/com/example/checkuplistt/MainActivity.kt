package com.example.checkuplistt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.checkuplistt.ui.screens.MediaTrackerApp
import com.example.checkuplistt.ui.theme.CheckuplisttTheme   // ✅ sin ".ui." extra

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckuplisttTheme {             // ✅ composable correcto
                MediaTrackerApp()
            }
        }
    }
}
