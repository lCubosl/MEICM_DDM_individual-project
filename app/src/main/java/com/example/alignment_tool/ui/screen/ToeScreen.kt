package com.example.alignment_tool.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.Offset
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import kotlin.math.atan2
import androidx.compose.ui.platform.LocalContext

// CALCULATETOE FUNCTION IS NOT WORKING CORRECTLY. MATH IS NOT MATHING
//fun calculateToe(offsetX: Float, offsetY: Float): Float {
//    // Replace with your real math!
//    return (offsetX * 10f)   // simple example
//}

@Composable
fun ToeScreen() {
    val context = LocalContext.current
    var selectedWheel by remember { mutableStateOf<String?>(null) }

    // RETHINK THIS STATE. IS NOT BEING IMPLEMENTED CORRECTLY
    // NEW: toe value state
    var toeValue by remember { mutableStateOf(0f) }

    val tilt = rememberTilt(context)
    val (offsetX, offsetY) = tilt.value

//    // Whenever selectedWheel changes → update toeValue
//    LaunchedEffect(selectedWheel) {
//        when (selectedWheel) {
//            "FL" -> toeValue = 0f          // FL always shows 0
//            "FR" -> toeValue = calculateToe(offsetX, offsetY)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // TOP ARROW
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "^",
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Front",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // MAIN CENTER COLUMN (front wheels + toe + bubble + rear wheels)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // FRONT WHEELS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
            ) {
                WheelWithLabel("FL", selectedWheel == "FL") { selectedWheel = "FL" }
                WheelWithLabel("FR", selectedWheel == "FR") { selectedWheel = "FR" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // THIS IS WRONG. CHANGE IT IN ORDER TO ACTUALLY DISPLAY TOE, NOT RANDOM NUMBERS
            // --- TOE TEXT HERE (centered between wheels) ---
            val showToe = selectedWheel == "FL" || selectedWheel == "FR"

            Text(
                text = "Toe:\n${toeValue}",
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(if (showToe) 1f else 0f)
            )
            // ----------------------------------------------------

            Spacer(modifier = Modifier.height(16.dp))

            // LEVEL BUBBLE
            LevelBubble(
                modifier = Modifier.size(150.dp),
                bubbleOffsetX = offsetX,
                bubbleOffsetY = offsetY
            )

            Spacer(modifier = Modifier.height(32.dp))

            // REAR WHEELS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
            ) {
                WheelWithLabel("RL", selectedWheel == "RL") { selectedWheel = "RL" }
                WheelWithLabel("RR", selectedWheel == "RR") { selectedWheel = "RR" }
            }
        }

        // BOTTOM BUTTON (back where it was!)
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Restart",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun rememberTilt(context: Context): State<Pair<Float, Float>> {
    val tilt = remember { mutableStateOf(0f to 0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // --- Add smoothing variables here ---
        var smoothX = 0f
        var smoothY = 0f
        val alpha = 0.1f // smoothing factor, smaller = smoother

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val rawX = event.values[0]
                val rawY = event.values[1]

                // --- Low-pass filter ---
                smoothX = alpha * (-rawX) + (1 - alpha) * smoothX
                smoothY = alpha * rawY + (1 - alpha) * smoothY

                // Normalize to -1..1 for UI
                val maxTilt = 9.8f
                val offsetX = (smoothX / maxTilt).coerceIn(-1f, 1f)
                val offsetY = (smoothY / maxTilt).coerceIn(-1f, 1f)

                tilt.value = offsetX to offsetY
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return tilt
}

@Composable
fun LevelBubble(
    modifier: Modifier = Modifier,
    bubbleOffsetX: Float = 0f,  // -1..1, horizontal tilt, can go beyond 1/-1
    bubbleOffsetY: Float = 0f   // -1..1, vertical tilt
) {
    val bubbleColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f

        // Outer circle slightly bigger than the dot (just for reference)
        val outerRadius = 20.dp.toPx()
        drawCircle(
            color = Color.LightGray,
            radius = outerRadius,
            center = Offset(radius, radius),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )

        // Outer circle slightly bigger than the dot (just for reference)
        val outerRadius2 = 20.dp.toPx() + 10.dp.toPx()
        drawCircle(
            color = Color.LightGray,
            radius = outerRadius2,
            center = Offset(radius, radius),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )

        // Outer circle slightly bigger than the dot (just for reference)
        val outerRadius3 = 20.dp.toPx() + 20.dp.toPx()
        drawCircle(
            color = Color.LightGray,
            radius = outerRadius3,
            center = Offset(radius, radius),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )

        // Dot inside (can move beyond outer circle)
        val dotRadius = 15.dp.toPx()
        val centerX = radius + bubbleOffsetX * radius // full radius range
        val centerY = radius + bubbleOffsetY * radius

        drawCircle(
            color = bubbleColor,
            radius = dotRadius,
            center = Offset(centerX, centerY)
        )
    }
}


@Composable
fun WheelWithLabel(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFF90CAF9) else Color.LightGray // Softer blue
    val textColor = Color.Black

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(100.dp)
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {}
    }
}