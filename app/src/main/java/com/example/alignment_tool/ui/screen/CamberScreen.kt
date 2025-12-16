package com.example.alignment_tool.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.example.alignment_tool.data.viewmodel.CamberViewModel
import com.example.alignment_tool.ui.components.bubble.LevelIndicator
import com.example.alignment_tool.ui.components.wheels.WheelButtonsPanel
import com.example.alignment_tool.ui.components.wheels.WheelSelector
import com.example.alignment_tool.ui.sensors.rememberOrientationAngles
import com.example.alignment_tool.ui.sensors.rememberTilt
import kotlin.collections.set
import com.example.alignment_tool.ui.components.controls.CamberControlsWithSave



// ----------------------------------------------------------
// MAIN CAMBER SCREEN WHERE @COMPOSABLES ARE CALLED
// ----------------------------------------------------------
@Composable
fun CamberScreen(viewModel: CamberViewModel) {
    var selectedWheel by remember { mutableStateOf("FL") }
    val tilt by rememberTilt(context = LocalContext.current)
    val orientation = rememberOrientationAngles(context = LocalContext.current)
    val roll = -orientation.value.third - 90f

    var showConfirmDialog by remember { mutableStateOf(false) }

    val savedCambers = remember {
        mutableStateMapOf<String, Float?>(
            "FL" to null,
            "FR" to null,
            "RL" to null,
            "RR" to null
        )
    }

    if (showConfirmDialog) {
        CamberControlsWithSave(
            onDismiss = { showConfirmDialog = false },
            onConfirm = {
                viewModel.saveMeasurement(
                    fl = savedCambers["FL"],
                    fr = savedCambers["FR"],
                    rl = savedCambers["RL"],
                    rr = savedCambers["RR"]
                )
                savedCambers.keys.forEach { it -> savedCambers[it] = null }
                showConfirmDialog = false
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val rotatedTilt = Pair(-tilt.first, -tilt.second)
        var lineColorGlobal by remember { mutableStateOf(Color.LightGray) }
        val allWheelsSaved = savedCambers.values.all { it != null }

        LevelIndicator(
            tilt = rotatedTilt,
            camber = roll,
            selectedWheel = selectedWheel,
            onLineColorChanged = { lineColorGlobal = it },
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth()
                .graphicsLayer { rotationZ = 90f }
        )

        Row(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WheelSelector(selectedWheel, savedCambers) { selectedWheel = it }

            WheelButtonsPanel(
                allWheelsSaved = allWheelsSaved,
                selectedWheel = selectedWheel,
                roll = roll,
                lineColor = lineColorGlobal,
                savedCambers = savedCambers,
                onSaveAllConfirmed = { showConfirmDialog = true }
            )
        }
    }
}