package com.example.checkuplistt.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object KawaiiColors {
    // Fondos base (suaves, nada blanco puro)
    val BgDark    = Color(0xFFFAF5FA) // rosa-lila niebla
    val BgLight   = Color(0xFFFDF7FB) // rosado lechoso

    // Colores pastel principales (femeninos y calmados)
    val Rose      = Color(0xFFEFC7D7) // rosa té
    val Lilac     = Color(0xFFDCC8F6) // lila suave
    val Peach     = Color(0xFFF9D9C2) // durazno crema
    val Mint      = Color(0xFFCFEDE6) // menta agua
    val Butter    = Color(0xFFFFF0C9) // vainilla
    val MistBlue  = Color(0xFFCADBF5) // azul neblina

    // Texto (siempre legible sobre pastel)
    val Ink       = Color(0xFF3A2E39) // ciruela muy oscuro (no negro puro)
    val InkMed    = Color(0xFF5C5560)
    val InkLight  = Color(0xFF8B8690)

    // Accents suaves (no chillones)
    val AccentRose  = Color(0xFFE6A6B6)
    val AccentLilac = Color(0xFFC7B3EE)
    val AccentMint  = Color(0xFFB9E3DA)

    // ---- Aliases para que todo tu código actual compile sin tocar nada ----
    val PastelPink   = Rose
    val PastelBlue   = MistBlue
    val PastelGreen  = Mint
    val PastelYellow = Butter
    val PastelPurple = Lilac
    val PastelMint   = Mint

    val TextDark    = Ink
    val TextMedium  = InkMed
    val TextLight   = InkLight

    val PinkSoft     = Rose
    val PurpleSoft   = Lilac
    val BlueSoft     = MistBlue
    val GreenSoft    = Mint
    val YellowSoft   = Butter
    val WhiteCream   = BgLight
    val GrayLight    = Color(0xFFE9E5EC)

    val PinkBright   = AccentRose
    val PurpleBright = AccentLilac

    // Compat con archivos anteriores
    val PixelCyan = PastelMint
    val PixelRed  = AccentRose
    val PixelPink = PastelPink
    val White     = Color(0xFFFFFFFF)
    val TextDim   = TextMedium
}

// Bordes más suaves (no cuadrados “pixel”)
val PixelShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)
