package agalfioni.profileavatareditor.avatar_editor.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NineAreaCircleGrid(
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Black,
    lineWidth: Dp = 2.dp,
    circleBorderWidth: Dp = 2.dp
) {
    // Convert Dp to Pixels for drawing commands
    val lineWidthPx = with(LocalDensity.current) { lineWidth.toPx() }
    val borderPx = with(LocalDensity.current) { circleBorderWidth.toPx() }

    Canvas(
        // Ensure the canvas is always a square based on its smallest dimension
        modifier = modifier.aspectRatio(1f)
    ) {
        // 1. Calculate Dimensions
        // Determine radius based on the smaller side to ensure it fits
        val radius = size.minDimension / 2f
        val diameter = radius * 2f
        val centerOffset = this.center

        // Find the top-left corner of the imaginary square bounding box
        val topLeft = Offset(
            x = centerOffset.x - radius,
            y = centerOffset.y - radius
        )

        // Calculate the step size for 1/3 and 2/3 marks
        val step = diameter / 3f

        // Define the circular path used for the border and clipping
        val circlePath = Path().apply {
            addOval(Rect(topLeft, Size(diameter, diameter)))
        }

        // 2. Draw the Grid Lines (Clipped inside the circle)
        // clipPath ensures anything drawn inside this block does not exceed the circle boundaries.
        // Coordinates for horizontal lines (at 1/3 height and 2/3 height)
        val y1 = topLeft.y + step
        val y2 = topLeft.y + (step * 2)

        // Coordinates for vertical lines (at 1/3 width and 2/3 width)
        val x1 = topLeft.x + step
        val x2 = topLeft.x + (step * 2)

        // Draw Horizontal Lines
        drawLine(
            color = lineColor,
            start = Offset(topLeft.x, y1),
            end = Offset(topLeft.x + diameter, y1),
            strokeWidth = lineWidthPx
        )
        drawLine(
            color = lineColor,
            start = Offset(topLeft.x, y2),
            end = Offset(topLeft.x + diameter, y2),
            strokeWidth = lineWidthPx
        )

        // Draw Vertical Lines
        drawLine(
            color = lineColor,
            start = Offset(x1, topLeft.y),
            end = Offset(x1, topLeft.y + diameter),
            strokeWidth = lineWidthPx
        )
        drawLine(
            color = lineColor,
            start = Offset(x2, topLeft.y),
            end = Offset(x2, topLeft.y + diameter),
            strokeWidth = lineWidthPx
        )

        // 3. Draw the Outer Circle Border
        drawPath(
            path = circlePath,
            color = lineColor,
            style = Stroke(width = borderPx)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NineAreaCirclePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        NineAreaCircleGrid(
            modifier = Modifier.size(300.dp),
            lineColor = Color.DarkGray,
            lineWidth = 1.5.dp
        )
    }
}