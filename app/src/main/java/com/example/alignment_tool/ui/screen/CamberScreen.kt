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

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.rememberTextMeasurer

@Composable
fun CamberScreen() {
    var selectedWheel by remember { mutableStateOf("FL") }
    val tilt by rememberTilt(context = LocalContext.current)
    val orientation = rememberOrientationAngles(context = LocalContext.current)
    val roll = - orientation.value.third - 90f

    // stores camber values after pressing SAVE button
    val savedCambers = remember {
        mutableStateMapOf<String, Float?>(
            "FL" to null,
            "FR" to null,
            "RL" to null,
            "RR" to null
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top 80%: Level indicator
        val rotatedTilt = Pair(-tilt.first, -tilt.second)
        var lineColorGlobal by remember { mutableStateOf(Color.LightGray) }

        LevelIndicator(
            tilt = rotatedTilt,
            camber = roll,
            selectedWheel = selectedWheel,
            onLineColorChanged = { lineColorGlobal = it },
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
                    SmallWheel(
                        label = "RL",
                        isSelected = selectedWheel == "RL",
                        savedCamber = savedCambers["RL"],
                    ) { selectedWheel = "RL" }
                    SmallWheel(
                        label = "FL",
                        isSelected = selectedWheel == "FL",
                        savedCamber = savedCambers["FL"],
                    ) { selectedWheel = "FL" }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // right side of car
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    SmallWheel(
                        label = "RR",
                        isSelected = selectedWheel == "RR",
                        savedCamber = savedCambers["RR"],
                    ) { selectedWheel = "RR" }
                    SmallWheel(
                        label = "FR",
                        isSelected = selectedWheel == "FR",
                        savedCamber = savedCambers["FR"],
                    ) { selectedWheel = "FR" }
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
                    onClick = {
                        if (lineColorGlobal == Color.Green) {   // <-- we will set lineColorGlobal
                            savedCambers[selectedWheel] = roll  // store the raw roll value
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
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

@Composable
fun rememberOrientationAngles(context: Context): State<Triple<Float, Float, Float>> {

    val orientation = remember { mutableStateOf(Triple(0f, 0f, 0f)) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientationAngles)

                val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat() // yaw
                val pitch   = Math.toDegrees(orientationAngles[1].toDouble()).toFloat() // pitch
                val roll    = Math.toDegrees(orientationAngles[2].toDouble()).toFloat() // roll

                orientation.value = Triple(azimuth, pitch, roll)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return orientation
}

@Composable
fun SmallWheel(
    label: String,              // <-- NEW: FL, FR, RL, RR
    isSelected: Boolean,
    savedCamber: Float?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 50.dp)
            .background(
                if (isSelected) Color(0xFF90CAF9) else Color.LightGray,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                rotationZ = 90f   // rotate both texts together
            }
        ) {
            // Wheel label (FL, FR, RL, RR)
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp
            )

            // Camber value
            if (savedCamber != null) {
                Text(
                    text = "%.1f°".format(savedCamber),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}