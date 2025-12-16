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
//  FRONT WHEELS
// ----------------------------------------------------------
@Composable
fun FrontWheelSection(
    selectedWheel: String?,
    wheelDisplay: Map<String, Float>,
    frontToe: Float?,
    onSelect: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // FRONT WHEELS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterHorizontally)
        ) {
            WheelWithLabelAndValue(
                "FL",
                selectedWheel == "FL",
                wheelDisplay["FL"]
            ) { onSelect("FL") }

            WheelWithLabelAndValue(
                "FR",
                selectedWheel == "FR",
                wheelDisplay["FR"]
            ) { onSelect("FR") }
        }

        // FRONT TOE VALUE
        Text(
            text = frontToe?.let { "Front Toe: %.2f°".format(it) } ?: "Front Toe: 0.00°",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF90CAF9),
            modifier = Modifier.alpha(if (frontToe != null) 1f else 0f)
        )
    }
}