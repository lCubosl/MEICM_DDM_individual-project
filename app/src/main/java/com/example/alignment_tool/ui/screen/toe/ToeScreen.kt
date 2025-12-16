package com.example.alignment_tool.ui.screen.toe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalContext
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.ui.components.bubble.BubbleCenterOverlay
import com.example.alignment_tool.ui.components.bubble.LevelBubble
import com.example.alignment_tool.ui.components.controls.ToeControlsWithSave
import com.example.alignment_tool.ui.components.wheels.FrontWheelSection
import com.example.alignment_tool.ui.components.wheels.RearWheelSection
import com.example.alignment_tool.ui.sensors.rememberTilt
import com.example.alignment_tool.ui.sensors.rememberYaw

// ----------------------------------------------------------
//  MAIN TOE SCREEN
// ----------------------------------------------------------
@Composable
fun ToeScreen(viewModel: ToeViewModel) {
    val context = LocalContext.current
    val yaw = rememberYaw(context).value
    val (offsetX, offsetY) = rememberTilt(context).value

    var selectedWheel by remember { mutableStateOf<String?>(null) }
    // Store individual wheel yaw readings
    val wheelYaw = remember { mutableStateMapOf<String, Float>() }

    var frontToe by remember { mutableStateOf<Float?>(null) }
    var rearToe by remember { mutableStateOf<Float?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // sanity check
            Text("yaw %.3f".format(yaw))

            // FRONT WHEELS
            FrontWheelSection(
                selectedWheel = selectedWheel,
                wheelDisplay = wheelYaw,
                frontToe = frontToe,
                onSelect = { selectedWheel = it }
            )
            // Level bubble
            LevelBubble(
                modifier = Modifier.size(150.dp),
                bubbleOffsetX = offsetX,
                bubbleOffsetY = offsetY
            )
            // REAR WHEELS
            RearWheelSection(
                selectedWheel = selectedWheel,
                wheelDisplay = wheelYaw,
                rearToe = rearToe,
                onSelect = { selectedWheel = it }
            )

            val allFrontSet = wheelYaw.containsKey("FL") && wheelYaw.containsKey("FR")
            val allRearSet = wheelYaw.containsKey("RL") && wheelYaw.containsKey("RR")
            val allWheelsSet = allFrontSet && allRearSet

            ToeControlsWithSave(
                isSetEnabled = selectedWheel != null,
                allWheelsSet = allWheelsSet,
                onSet = {
                    selectedWheel?.let { wheel ->
                        wheelYaw[wheel] = yaw
                        wheelYaw[wheel] = yaw
                        selectedWheel = null

                        // Calculate front toe if both FL and FR are set
                        frontToe = calculateFrontToe(wheelYaw)
                        // Calculate rear toe if both FL and FR are set
                        rearToe = calculateRearToe(wheelYaw)
                    }
                },
                onSave = {
                    viewModel.saveMeasurement(
                        flAngle = wheelYaw["FL"],
                        frAngle = wheelYaw["FR"],
                        rlAngle = wheelYaw["RL"],
                        rrAngle = wheelYaw["RR"],
                        fToe = frontToe,
                        rToe = rearToe
                    )

                    wheelYaw.clear()
                    wheelYaw.clear()
                    frontToe = null
                    rearToe = null
                    selectedWheel = null
                },
                onRestart = {
                    wheelYaw.clear()
                    wheelYaw.clear()
                    frontToe = null
                    rearToe = null
                    selectedWheel = null
                }
            )
        }

        BubbleCenterOverlay(bubbleOffsetX = offsetX, bubbleOffsetY = offsetY)
    }
}

