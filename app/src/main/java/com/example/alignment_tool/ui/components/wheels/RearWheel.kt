package com.example.alignment_tool.ui.components.wheels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------------
//  REAR WHEELS
// ----------------------------------------------------------
@Composable
fun RearWheelSection(
    selectedWheel: String?,
    wheelDisplay: Map<String, Float>,
    rearToe: Float?,
    onSelect: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // REAR WHEELS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
        ) {
            WheelWithLabelAndValue(
                "RL",
                selectedWheel == "RL",
                wheelDisplay["RL"]
            ) { onSelect("RL") }

            WheelWithLabelAndValue(
                "RR",
                selectedWheel == "RR",
                wheelDisplay["RR"]
            ) { onSelect("RR") }
        }

        // REAR TOE VALUE
        Text(
            text = rearToe?.let { "Rear Toe: %.2f°".format(it) } ?: "Rear Toe: 0.00°",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF90CAF9),
            modifier = Modifier.alpha(if (rearToe != null) 1f else 0f)
        )
    }
}