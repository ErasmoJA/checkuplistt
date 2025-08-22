package com.example.checkuplistt.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

private val DreamyTypography = Typography().copy(
    titleLarge   = Typography().titleLarge.copy(fontFamily = FontFamily.SansSerif),
    titleMedium  = Typography().titleMedium.copy(fontFamily = FontFamily.SansSerif),
    bodyLarge    = Typography().bodyLarge.copy(fontFamily = FontFamily.SansSerif),
    bodyMedium   = Typography().bodyMedium.copy(fontFamily = FontFamily.SansSerif),
    labelLarge   = Typography().labelLarge.copy(fontFamily = FontFamily.SansSerif),
)

@Composable
fun CheckuplisttTheme(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme.copy(
        primary     = KawaiiColors.AccentRose,
        secondary   = KawaiiColors.AccentLilac,
        background  = KawaiiColors.BgDark,
        surface     = KawaiiColors.BgLight,
        onPrimary   = KawaiiColors.Ink,     // texto oscuro sobre botones pastel
        onSecondary = KawaiiColors.Ink,
        onBackground= KawaiiColors.Ink,     // texto general oscuro
        onSurface   = KawaiiColors.Ink
    )
    MaterialTheme(
        colorScheme = colors,
        typography  = DreamyTypography,
        shapes      = PixelShapes,
        content     = content
    )
}
