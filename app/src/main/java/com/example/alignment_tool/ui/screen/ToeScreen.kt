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
import com.example.alignment_tool.data.viewmodel.ToeViewModel

// ----------------------------------------------------------
// REMEMBER PHONE YAW (heading) using the Rotation Vector sensor
// ----------------------------------------------------------
@Composable
fun rememberYaw(context: Context): State<Float> {
    val yawState = remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val listener = object : SensorEventListener {
            val rot = FloatArray(9)
            val ori = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rot, event.values)
                SensorManager.getOrientation(rot, ori)
                var yaw = Math.toDegrees(ori[0].toDouble()).toFloat()
                if (yaw < 0) yaw += 360f
                yawState.floatValue = yaw
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        onDispose { sm.unregisterListener(listener) }
    }
    return yawState
}

// ----------------------------------------------------------
//  REMEMBER TILT for LevelBubble (your old implementation)
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
fun ToeScreen(viewModel: ToeViewModel) {
    val context = LocalContext.current
    val yaw = rememberYaw(context).value
    val tilt = rememberTilt(context).value
    val (offsetX, offsetY) = tilt

    var selectedWheel by remember { mutableStateOf<String?>(null) }

    // Store individual wheel yaw readings
    val wheelYaw = remember { mutableStateMapOf<String, Float>() }

    // Store temporary displayed values under each wheel
    val wheelDisplay = remember { mutableStateMapOf<String, Float>() }

    var frontToe by remember { mutableStateOf<Float?>(null) }
    var rearToe by remember { mutableStateOf<Float?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // FRONT OF THE CAR
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Front", fontSize = 22.sp, color = Color(0xFF90CAF9), fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // FRONT WHEELS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
                ) {
                    WheelWithLabelAndValue("FL", selectedWheel == "FL", wheelDisplay["FL"]) { selectedWheel = "FL" }
                    WheelWithLabelAndValue("FR", selectedWheel == "FR", wheelDisplay["FR"]) { selectedWheel = "FR" }
                }

                // Front toe display (calculated after both front wheels set)
                // Reserve fixed space for Front Toe display
                Text(
                    text = frontToe?.let { "Front Toe: %.2f°".format(it) } ?: "Front Toe: 0.00°",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF90CAF9),
                    modifier = Modifier.alpha(if (frontToe != null) 1f else 0f)
                )

                // Level bubble
                LevelBubble(modifier = Modifier.size(150.dp), bubbleOffsetX = offsetX, bubbleOffsetY = offsetY)

                Spacer(modifier = Modifier.height(16.dp))

                // REAR WHEELS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
                ) {
                    WheelWithLabelAndValue("RL", selectedWheel == "RL", wheelDisplay["RL"]) { selectedWheel = "RL" }
                    WheelWithLabelAndValue("RR", selectedWheel == "RR", wheelDisplay["RR"]) { selectedWheel = "RR" }
                }


                // Rear toe display (calculated after both rear wheels set)
                Text(
                    text = rearToe?.let { "Rear Toe: %.2f°".format(it) } ?: "Rear Toe: 0.00°",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF90CAF9),
                    modifier = Modifier.alpha(if (rearToe != null) 1f else 0f)
                )

            }

            val allFrontSet = wheelDisplay.containsKey("FL") && wheelDisplay.containsKey("FR")
            val allRearSet = wheelDisplay.containsKey("RL") && wheelDisplay.containsKey("RR")
            val allWheelsSet = allFrontSet && allRearSet

            ToeControlsWithSave(
                isSetEnabled = selectedWheel != null,
                allWheelsSet = allWheelsSet,
                onSet = {
                    selectedWheel?.let { wheel ->
                        wheelYaw[wheel] = yaw
                        wheelDisplay[wheel] = yaw
                        selectedWheel = null

                        // Calculate front toe if both FL and FR are set
                        val fl = wheelYaw["FL"]
                        val fr = wheelYaw["FR"]
                        if (fl != null && fr != null) frontToe = calculateToeAngle(fl, fr, invert = true)

                        // Calculate rear toe if both RL and RR are set
                        val rl = wheelYaw["RL"]
                        val rr = wheelYaw["RR"]
                        if (rl != null && rr != null) rearToe = calculateToeAngle(rl, rr, invert = false)
                    }
                },
                onSave = {
                    viewModel.saveMeasurement(
                        flAngle = wheelDisplay["FL"],
                        frAngle = wheelDisplay["FR"],
                        rlAngle = wheelDisplay["RL"],
                        rrAngle = wheelDisplay["RR"],
                        fToe = frontToe,
                        rToe = rearToe
                    )

                    wheelYaw.clear()
                    wheelDisplay.clear()
                    frontToe = null
                    rearToe = null
                    selectedWheel = null
                },
                onRestart = {
                    wheelYaw.clear()
                    wheelDisplay.clear()
                    frontToe = null
                    rearToe = null
                    selectedWheel = null
                }
            )
        }

        BubbleCenterOverlay(bubbleOffsetX = offsetX, bubbleOffsetY = offsetY)
    }
}

@Composable
fun WheelWithLabelAndValue(
    label: String,
    isSelected: Boolean,
    value: Float?,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WheelWithLabel(label = label, isSelected = isSelected, onClick = onClick)

        // Reserve fixed height for the value text
        Text(
            text = value?.let { "%.2f°".format(it) } ?: "",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.height(20.dp), // Reserve space so layout doesn't change
            textAlign = TextAlign.Center,
            color = Color(0xFF90CAF9).copy(alpha = if (value != null) 1f else 0f)
        )
    }
}

// ----------------------------------------------------------
//  WHEEL BUTTON
// ----------------------------------------------------------
@Composable
fun WheelWithLabel(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val wheelBackground = if (isSelected) Color(0xFF90CAF9) else Color.LightGray
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
            color = MaterialTheme.colorScheme.background,
            fontSize = 20.sp
        )
    }
}

// ----------------------------------------------------------
//  TOE BUTTONS
// ----------------------------------------------------------
@Composable
fun ToeControlsWithSave(
    isSetEnabled: Boolean,
    allWheelsSet: Boolean,
    onSet: () -> Unit,
    onSave: () -> Unit,
    onRestart: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (allWheelsSet) {
            // Row with SET + SAVE buttons
            Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // SET button - 2/3 width
                Button(
                    onClick = onSet,
                    enabled = isSetEnabled,
                    modifier = Modifier.weight(2f).height(56.dp)
                ) {
                    Text("SET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // SAVE button - 1/3 width, green
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("SAVE", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Only SET button
            Button(
                onClick = onSet,
                enabled = isSetEnabled,
                modifier = Modifier.fillMaxWidth(0.8f).height(56.dp)
            ) {
                Text("SET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Restart button below
        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth(0.8f).height(56.dp)
        ) {
            Text("Restart", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Save All Toe Values?") },
            text = { Text("Are you sure you want to save all current toe measurements?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSave()  // call the save logic
                        showConfirmDialog = false
                    }
                ) {
                    Text("Yes", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No", color = Color.Black)
                }
            }
        )
    }
}

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

// ----------------------------------------------------------
//  BUBBLE CENTER OVERLAY
// ----------------------------------------------------------
@Composable
fun BubbleCenterOverlay(modifier: Modifier = Modifier, bubbleOffsetX: Float, bubbleOffsetY: Float, centeredThreshold: Float = 0.10f) {
    val distance = sqrt(bubbleOffsetX * bubbleOffsetX + bubbleOffsetY * bubbleOffsetY)
    val centered = distance < centeredThreshold

    if (!centered) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
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
//  CALCULATE TOE ANGLE
// ----------------------------------------------------------
fun calculateToeAngle(left: Float, right: Float, invert: Boolean = false): Float {
    // Shortest signed angle from left to right
    var delta = ((right - left + 540) % 360) - 180
    if (invert) delta *= -1   // Use for front wheels
    return delta
}