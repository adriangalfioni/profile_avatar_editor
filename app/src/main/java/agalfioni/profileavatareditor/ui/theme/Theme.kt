package agalfioni.profileavatareditor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    // 1. Primary Brand Color
    primary = PrimaryBlue,
    onPrimary = TextOnPrimary,

    // 2. Backgrounds
    background = BackgroundLight,
    onBackground = TextPrimary,

    // 3. Cards / Sheets (Surface)
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight, // Fallback for variant areas
    onSurfaceVariant = TextSecondary, // "text-secondary" fits perfectly here

    // 4. Special "Surface Alt" (Dark surface in light theme)
    inverseSurface = SurfaceAlt,
    inverseOnSurface = Color.White, // Text on the dark alt surface

    // 5. Outlines / Borders
    outline = OutlineLight,

    // 6. Errors
    error = ErrorRed,
    onError = Color.White,
    errorContainer = SurfaceError, // "surface-error"
    onErrorContainer = ErrorRed
)

@Composable
fun ProfileAvatarEditorTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}