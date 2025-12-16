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
// TODO
// ----------------------------------------------------------
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