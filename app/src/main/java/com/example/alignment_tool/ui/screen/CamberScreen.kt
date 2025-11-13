package com.example.alignment_tool.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.toArgb

@Composable
fun CamberScreen() {
    var selectedWheel by remember { mutableStateOf("FL") }
    val tilt by rememberTilt(context = LocalContext.current)

    Column(modifier = Modifier.fillMaxSize()) {
        // Top 80%: Level indicator
        LevelIndicator(
            tilt = tilt,
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth()
                .graphicsLayer { rotationZ = 90f },
        )

        // diagram and buttons
        Row(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: small wheel diagram
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // left side of car
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    SmallWheel(isSelected = selectedWheel == "RL") { selectedWheel = "RL" }
                    SmallWheel(isSelected = selectedWheel == "FL") { selectedWheel = "FL" }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // right side of car
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    SmallWheel(isSelected = selectedWheel == "RR") { selectedWheel = "RR" }
                    SmallWheel(isSelected = selectedWheel == "FR") { selectedWheel = "FR" }
                }
            }

            // Right: buttons column rotated 90 degrees
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .graphicsLayer { rotationZ = 90f }, // rotate 90 degrees clockwise
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* TODO Save */ },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    Text("Save")
                }

                Button(
                    onClick = { /* TODO Back */ },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    Text("Back")
                }
            }

        }
    }
}

@Composable
fun LevelIndicator(tilt: Pair<Float, Float>, modifier: Modifier = Modifier) {
    // tilt.first = x-axis, tilt.second = y-axis, normalized -1..1
    val angle = atan2(tilt.second.toDouble(), tilt.first.toDouble()).toFloat() // in radians
    val camberAngle: Float = Math.toDegrees(
        atan2(tilt.second.toDouble(), tilt.first.toDouble())
    ).toFloat() - 90f
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val centerX = width / 2
        val lineLength = width * 0.95f

        // Grey horizontal line at center
        drawLine(
            color = Color.LightGray,
            start = Offset(centerX - lineLength / 2, centerY),
            end = Offset(centerX + lineLength / 2, centerY),
            strokeWidth = 8f
        )

        // Blue line representing phone tilt
        val tiltLineLength = lineLength
        val cosA = kotlin.math.cos(angle)
        val sinA = kotlin.math.sin(angle)

        drawLine(
            color = Color(0xFF90CAF9),
            start = Offset(
                centerX - tiltLineLength / 2 * cosA,
                centerY - tiltLineLength / 2 * sinA
            ),
            end = Offset(centerX + tiltLineLength / 2 * cosA, centerY + tiltLineLength / 2 * sinA),
            strokeWidth = 8f
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "Level:",
                26f,                  // x-coordinate
                centerY - 20f,         // y-coordinate, adjust to vertical center on line
                android.graphics.Paint().apply {
                    color = Color.LightGray.toArgb()
                    textSize = 48f    // adjust as needed
                    isAntiAlias = true
                }
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
                color = Color.LightGray.toArgb()
                textSize = 48f
                isAntiAlias = true
            }
            drawText(
                "Level: %.1f".format(camberAngle),
                26f,                  // x-coordinate
                centerY - 20f,        // y-coordinate
                paint
            )
        }
    }
}

@Composable
fun SmallWheel(isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF90CAF9) else Color.LightGray

    Box(
        modifier = Modifier
            .size(width = 60.dp, height = 30.dp) // swap width/height for horizontal look
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    )
}

