package com.example.alignment_tool.ui.components.wheels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

// ----------------------------------------------------------
// BUTTONS PANEL @COMPOSABLE
// ----------------------------------------------------------
@Composable
fun WheelButtonsPanel(
    allWheelsSaved: Boolean,
    selectedWheel: String,
    roll: Float,
    lineColor: Color,
    savedCambers: MutableMap<String, Float?>,
    onSaveAllConfirmed: () -> Unit,
) {
    // Right: buttons column rotated 90 degrees
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .graphicsLayer { rotationZ = 90f },
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // SAVE CAMBER TO WHEEL BUTTON
        Button(
            onClick = {
                if (!allWheelsSaved) {
                    // Normal Save for current wheel
                    if (lineColor == Color.Green) {
                        savedCambers[selectedWheel] = roll
                    }
                } else {
                    // Trigger confirmation dialog
                    onSaveAllConfirmed()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .weight(0.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (allWheelsSaved) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (allWheelsSaved) "Save All" else "Save", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { savedCambers[selectedWheel] = null },
            enabled = savedCambers[selectedWheel] != null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .weight(0.5f)
        ) {
            Text("Reset")
        }
    }
}