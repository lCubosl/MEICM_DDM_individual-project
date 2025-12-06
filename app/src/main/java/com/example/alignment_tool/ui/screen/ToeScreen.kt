package com.example.alignment_tool.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.Offset
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.ui.platform.LocalContext
import kotlin.math.sqrt
import androidx.compose.ui.graphics.drawscope.Stroke

// ----------------------------------------------------------
//  FUNCTION: Yaw (heading) using the Rotation Vector sensor
// ----------------------------------------------------------
@Composable
fun rememberYaw(context: Context): State<Float> {
    val yawState = remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val listener = object : SensorEventListener {
            val rotationMatrix = FloatArray(9)
            val orientationAngles = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientationAngles)

                val yawRad = orientationAngles[0]
                var yawDeg = Math.toDegrees(yawRad.toDouble()).toFloat()

                if (yawDeg < 0) yawDeg += 360f

                yawState.floatValue = yawDeg
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_GAME)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return yawState
}

// ----------------------------------------------------------
//  FUNCTION: Tilt for LevelBubble (your old implementation)
// ----------------------------------------------------------
@Composable
fun rememberTilt(context: Context): State<Pair<Float, Float>> {
    val tilt = remember { mutableStateOf(0f to 0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var smoothX = 0f
        var smoothY = 0f
        val alpha = 0.1f

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val rawX = event.values[0]
                val rawY = event.values[1]

                smoothX = alpha * (-rawX) + (1 - alpha) * smoothX
                smoothY = alpha * rawY + (1 - alpha) * smoothY

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

// ----------------------------------------------------------
//  MAIN TOE SCREEN
// ----------------------------------------------------------
@Composable
fun ToeScreen() {
    val context = LocalContext.current

    val yaw = rememberYaw(context).value
    val tilt = rememberTilt(context).value
    val (offsetX, offsetY) = tilt

    var selectedWheel by remember { mutableStateOf<String?>(null) }

    // store yaw measurements per wheel
    val wheelYaw = remember { mutableStateMapOf<String, Float>() }

    // toe result (degrees)
    var frontToe by remember { mutableStateOf<Float?>(null) }

    // toe visibility
    var showToe by remember { mutableStateOf(false) }

    fun calculateToeAngle(left: Float, right: Float): Float {
        var delta = right - left
        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f
        return delta
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // TOP LABEL
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("^", fontSize = 36.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text("Front", fontSize = 22.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // FRONT WHEELS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
            ) {
                WheelWithLabel("FL", selectedWheel == "FL") {
                    selectedWheel = "FL"
                    wheelYaw["FL"] = yaw

                    showToe = true
                    frontToe = 0f  // First wheel selected → show 0°

                    wheelYaw["FR"]?.let { frYaw ->
                        frontToe = calculateToeAngle(wheelYaw["FL"]!!, frYaw)
                    }
                }

                WheelWithLabel("FR", selectedWheel == "FR") {
                    selectedWheel = "FR"
                    wheelYaw["FR"] = yaw

                    showToe = true
                    frontToe = 0f

                    wheelYaw["FL"]?.let { flYaw ->
                        frontToe = calculateToeAngle(flYaw, wheelYaw["FR"]!!)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TOE VALUE DISPLAY
            if (showToe) {
                val toeText = "Front Toe:\n${"%.2f".format(frontToe ?: 0f)}°"

                Text(
                    text = toeText,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LevelBubble(
                modifier = Modifier.size(150.dp),
                bubbleOffsetX = offsetX,
                bubbleOffsetY = offsetY
            )

            Spacer(modifier = Modifier.height(32.dp))

            // REAR WHEELS (placeholders)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
            ) {
                WheelWithLabel("RL", selectedWheel == "RL") {}
                WheelWithLabel("RR", selectedWheel == "RR") {}
            }
        }

        // RESET BUTTON
        Button(
            onClick = {
                wheelYaw.clear()
                frontToe = null
                showToe = false
                selectedWheel = null
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Restart", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}



// ----------------------------------------------------------
//  LEVEL BUBBLE  (your colored bubble logic included)
// ----------------------------------------------------------
@Composable
fun LevelBubble(
    modifier: Modifier = Modifier,
    bubbleOffsetX: Float,
    bubbleOffsetY: Float
) {
    val distance = sqrt(bubbleOffsetX * bubbleOffsetX + bubbleOffsetY * bubbleOffsetY)

    val bubbleColor = when {
        distance < 0.10f -> Color(0xFF4CAF50)
        distance < 0.40f -> Color(0xFF90CAF9)
        else -> Color.Red
    }

    Canvas(modifier = modifier) {
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

// ----------------------------------------------------------
//  WHEEL BUTTONS
// ----------------------------------------------------------
@Composable
fun WheelWithLabel(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val background = if (isSelected) Color(0xFF90CAF9) else Color.LightGray

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.Bold, color = Color.Black)

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(100.dp)
                .background(background, RoundedCornerShape(12.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {}
    }
}