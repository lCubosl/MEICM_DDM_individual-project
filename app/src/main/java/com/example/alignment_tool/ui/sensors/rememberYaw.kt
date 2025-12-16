package com.example.alignment_tool.ui.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember

// ----------------------------------------------------------
// REMEMBER PHONE YAW (heading) using the Rotation Vector sensor
// yaw is very sensitive and can be affected by metal objects, nearby electronics, etc,
// yaw is not a good way to track toe since TYPE_ROTATION_VECTOR is a combination of gyro, accelerometer and Magnetometer
// gyro (noisy, sensitive and drifts), magnetometer (noisy, affected by metal and electronics)
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