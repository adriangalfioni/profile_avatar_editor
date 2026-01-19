package agalfioni.profileavatareditor.avatar_editor.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.cornerBorder(
    color: Color,
    width: Dp = 2.dp,       // Thickness of the lines
    cornerLength: Dp = 30.dp // How long the corner arms are
) = drawWithContent {
    // 1. Draw the actual content (The image/grid inside) first
    drawContent()

    val strokePx = width.toPx()
    val lenPx = cornerLength.toPx()

    // 2. Prepare the Stroke style (Round caps look smoother for UI overlays)
    val strokeStyle = Stroke(width = strokePx, cap = StrokeCap.Round)

    // 3. Offset to keep the border fully INSIDE the box (avoid clipping)
    val halfStroke = strokePx / 2f
    val w = size.width
    val h = size.height

    // 4. Draw the 4 Corners
    // Top-Left
    drawPath(
        path = Path().apply {
            moveTo(halfStroke, lenPx)      // Start lower
            lineTo(halfStroke, halfStroke) // Go up to corner
            lineTo(lenPx, halfStroke)      // Go right
        },
        color = color,
        style = strokeStyle
    )

    // Top-Right
    drawPath(
        path = Path().apply {
            moveTo(w - lenPx, halfStroke)
            lineTo(w - halfStroke, halfStroke)
            lineTo(w - halfStroke, lenPx)
        },
        color = color,
        style = strokeStyle
    )

    // Bottom-Right
    drawPath(
        path = Path().apply {
            moveTo(w - halfStroke, h - lenPx)
            lineTo(w - halfStroke, h - halfStroke)
            lineTo(w - lenPx, h - halfStroke)
        },
        color = color,
        style = strokeStyle
    )

    // Bottom-Left
    drawPath(
        path = Path().apply {
            moveTo(lenPx, h - halfStroke)
            lineTo(halfStroke, h - halfStroke)
            lineTo(halfStroke, h - lenPx)
        },
        color = color,
        style = strokeStyle
    )
}