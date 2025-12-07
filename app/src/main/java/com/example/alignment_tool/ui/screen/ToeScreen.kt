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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.alpha
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
    val wheelYaw = remember { mutableStateMapOf<String, Float>() }

    var frontToe by remember { mutableStateOf<Float?>(null) }
    var showFrontToe by remember { mutableStateOf(false) }

    var rearToe by remember { mutableStateOf<Float?>(null) }
    var showRearToe by remember { mutableStateOf(false) }

    fun calculateToeAngle(left: Float, right: Float): Float {
        var delta = right - left
        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f
        return delta
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Top Label
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "^",
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Front",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Front Wheels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
                ) {
                    WheelWithLabel("FL", selectedWheel == "FL") { selectedWheel = "FL" }
                    WheelWithLabel("FR", selectedWheel == "FR") { selectedWheel = "FR" }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val toeText = "Front Toe:${"%.2f".format(frontToe ?: 0f)}°"
                Text(
                    text = toeText,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alpha(if (showFrontToe) 1f else 0f)
                )

                // Level Bubble
                LevelBubble(
                    modifier = Modifier.size(150.dp),
                    bubbleOffsetX = offsetX,
                    bubbleOffsetY = offsetY
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Rear Wheels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
                ) {
                    WheelWithLabel("RL", selectedWheel == "RL") { selectedWheel = "RL" }
                    WheelWithLabel("RR", selectedWheel == "RR") { selectedWheel = "RR" }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val rearToeText = "Rear Toe:${"%.2f".format(rearToe ?: 0f)}°"
                Text(
                    text = rearToeText,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alpha(if (showRearToe) 1f else 0f)
                )
            }

            ToeControls(
                isSetEnabled = selectedWheel != null,
                onSet = {
                    selectedWheel?.let { wheel ->
                        wheelYaw[wheel] = yaw

                        if (wheel == "FL" || wheel == "FR") {
                            showFrontToe = true
                            val fl = wheelYaw["FL"]
                            val fr = wheelYaw["FR"]
                            frontToe = if (fl != null && fr != null)
                                calculateToeAngle(fl, fr)
                            else 0f
                        }

                        if (wheel == "RL" || wheel == "RR") {
                            showRearToe = true
                            val rl = wheelYaw["RL"]
                            val rr = wheelYaw["RR"]
                            rearToe = if (rl != null && rr != null)
                                calculateToeAngle(rl, rr)
                            else 0f
                        }
                    }
                },
                onRestart = {
                    wheelYaw.clear()
                    frontToe = null
                    showFrontToe = false
                    rearToe = null
                    showRearToe = false
                    selectedWheel = null
                }
            )
        }

        BubbleCenterOverlay(
            bubbleOffsetX = offsetX,
            bubbleOffsetY = offsetY
        )
    }
}

// ----------------------------------------------------------
//  LEVEL BUBBLE CENTER ALERT OVERLAY
// ----------------------------------------------------------
@Composable
fun BubbleCenterOverlay(
    modifier: Modifier = Modifier,
    bubbleOffsetX: Float,
    bubbleOffsetY: Float,
    centeredThreshold: Float = 0.10f
) {
    val distance = sqrt(bubbleOffsetX * bubbleOffsetX + bubbleOffsetY * bubbleOffsetY)
    val centered = distance < centeredThreshold

    if (!centered) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
                // This prevents clicks from passing through
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Wait for the Level Bubble to be near center before clicking \"SET\" the toe value Button.",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier.padding(34.dp)
            )
        }
    }
}


// ----------------------------------------------------------
//  TOE BUTTONS SET AND RESTART
// ----------------------------------------------------------
@Composable
fun ToeControls(
    isSetEnabled: Boolean,
    onSet: () -> Unit,
    onRestart: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Button(
            onClick = onSet,
            enabled = isSetEnabled,          // ← greyed out when false
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
        ) {
            Text("SET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
        ) {
            Text("Restart", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ----------------------------------------------------------
//  LEVEL BUBBLE
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

// ----------------------------------------------------------
//  WHEEL BUTTONS
// ----------------------------------------------------------
@Composable
fun WheelWithLabel(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val wheelBackground = if (isSelected) Color(0xFF90CAF9) else Color.LightGray
    val textColor = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(100.dp)
            .background(wheelBackground, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = textColor, // <-- text is invisible unless background changes
            fontSize = 20.sp
        )
    }
}