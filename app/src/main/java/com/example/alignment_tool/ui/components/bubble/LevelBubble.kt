package com.example.alignment_tool.ui.components.bubble

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

// ----------------------------------------------------------
//  LEVEL BUBBLE
// ----------------------------------------------------------
@Composable
fun LevelBubble(modifier: Modifier = Modifier, bubbleOffsetX: Float, bubbleOffsetY: Float) {
    val distance = sqrt(bubbleOffsetX * bubbleOffsetX + bubbleOffsetY * bubbleOffsetY)
    val bubbleColor = when {
        distance < 0.10f -> Color(0xFF4CAF50)
        distance < 0.40f -> Color(0xFF90CAF9)
        else -> Color.Red
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f
            val r1 = 20.dp.toPx()
            val r2 = r1 + 10.dp.toPx()
            val r3 = r1 + 20.dp.toPx()

            drawCircle(Color.LightGray, r1, Offset(radius, radius), style = Stroke(4f))
            drawCircle(Color.LightGray, r2, Offset(radius, radius), style = Stroke(4f))
            drawCircle(Color.LightGray, r3, Offset(radius, radius), style = Stroke(4f))

            val dotRadius = 15.dp.toPx()
            val cx = radius + bubbleOffsetX * radius
            val cy = radius + bubbleOffsetY * radius
            drawCircle(bubbleColor, dotRadius, Offset(cx, cy))
        }
    }
}