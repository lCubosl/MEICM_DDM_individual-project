package com.example.alignment_tool.ui.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

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