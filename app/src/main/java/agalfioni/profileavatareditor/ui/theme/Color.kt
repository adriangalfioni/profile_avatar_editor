package agalfioni.profileavatareditor.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Backgrounds & Surfaces
val BackgroundLight = Color(0xFFEFF1F4)  // "bg"
val SurfaceLight = Color(0xFFFFFFFF)     // "surface"
val SurfaceAlt = Color(0xFF13202F)       // "surface-alt" (Likely Inverse Surface)
val SurfaceError = Color(0xFFFFEAF0)     // "surface-error"

// Outlines
val OutlineLight = Color(0xFFC0CAD6)     // "outline"

// Text / Content
val TextPrimary = Color(0xFF0A131D)      // "text-primary"
val TextSecondary = Color(0xFF344054)    // "text-secondary"
val TextOnPrimary = Color(0xFFFFFFFF)    // "text-on-primary"

// Brand / Actions
val PrimaryBlueLight = Color(0xFF8AA4FF)
val PrimaryBlue = Color(0xFF6184FF)      // Using the darker end for better contrast

// Error
val ErrorRed = Color(0xFFCE003E)         // "error"


val PrimaryGradient = Brush.verticalGradient(
    colors = listOf(
        PrimaryBlueLight, // #8AA4FF
        PrimaryBlue       // #6184FF
    )
)