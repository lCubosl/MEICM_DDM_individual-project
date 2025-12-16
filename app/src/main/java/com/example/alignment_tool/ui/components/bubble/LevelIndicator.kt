package com.example.alignment_tool.ui.components.bubble

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlin.math.atan2

// ----------------------------------------------------------
// LEVEL INDICATOR. INDICATES PHONE TILT (ROLL)
// ----------------------------------------------------------
@Composable
fun LevelIndicator(
    tilt: Pair<Float, Float>,
    camber: Float,
    selectedWheel: String,
    onLineColorChanged: (Color) -> Unit,   // NEW
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    // Clamp camber value for display
    val displayCamber = camber.coerceIn(-20f, 20f)

    // Step 1: raw tilt in radians
    val rawAngle = atan2(tilt.second.toDouble(), tilt.first.toDouble()).toFloat()

    // Step 2: rotate raw tilt for display (180° rotation)
    val rotatedRawAngle = rawAngle + Math.PI.toFloat()

    // Step 3: clamp AFTER rotation
    val minAngleRad = Math.toRadians(155.0).toFloat()
    val maxAngleRad = Math.toRadians(205.0).toFloat()
    val clampedAngle = rotatedRawAngle.coerceIn(minAngleRad, maxAngleRad)

    // Step 4: determine line color
    val perfectRad = Math.PI.toFloat()   // 180°
    val greenZone = Math.toRadians(2.0).toFloat() // ±2° in radians

    val lineColor = when {
        rotatedRawAngle < minAngleRad || rotatedRawAngle > maxAngleRad -> Color.Red
        kotlin.math.abs(rotatedRawAngle - perfectRad) <= greenZone -> Color.Green
        else -> Color(0xFF90CAF9)
    }

    LaunchedEffect(lineColor) {
        onLineColorChanged(lineColor)
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val centerX = width / 2
        val lineLength = width * 0.95f

        // Grey horizontal line
        drawLine(
            color = Color.LightGray,
            start = Offset(centerX - lineLength / 2, centerY),
            end = Offset(centerX + lineLength / 2, centerY),
            strokeWidth = 8f
        )

        // Tilt line with color depending on out-of-bounds / perfect
        val cosA = kotlin.math.cos(clampedAngle)
        val sinA = kotlin.math.sin(clampedAngle)
        drawLine(
            color = lineColor,
            start = Offset(centerX - lineLength / 2 * cosA, centerY - lineLength / 2 * sinA),
            end = Offset(centerX + lineLength / 2 * cosA, centerY + lineLength / 2 * sinA),
            strokeWidth = 8f
        )

        // Warning text on top if tilt line is not green
        if (lineColor != Color.Green) {
            drawText(
                textMeasurer = textMeasurer,
                text = "Please align phone until the level color is green or both lines are aligned",
                topLeft = Offset(26f, centerY - 400f), // adjust vertical position
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 20.sp // smaller font
                )
            )
        }

        // Level text
        drawContext.canvas.nativeCanvas.drawText(
            "Level:",
            26f,
            centerY - 20f,
            android.graphics.Paint().apply {
                color = Color.LightGray.toArgb()
                textSize = 48f
                isAntiAlias = true
            }
        )

        // Compute tilt line color based on its rotation
        val lineColor2 = if (displayCamber in -19.9..19.9) Color(0xFF90CAF9) else Color.Red

        // Display camber value
        drawText(
            textMeasurer = textMeasurer,
            text = "$selectedWheel Camber: %.1f°".format(displayCamber),
            topLeft = Offset(26f, centerY + 120f),
            style = TextStyle(
                color = lineColor2,
                fontSize = 48.sp
            )
        )
    }
}