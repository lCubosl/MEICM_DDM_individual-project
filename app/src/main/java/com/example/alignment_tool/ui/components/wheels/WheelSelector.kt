package com.example.alignment_tool.ui.components.wheels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ----------------------------------------------------------
// WHEEL SELECTOR @COMPOSABLES
// ----------------------------------------------------------
@Composable
fun WheelSelector(
    selectedWheel: String,
    savedCambers: Map<String, Float?>,
    onWheelSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            SmallWheel("RL", selectedWheel == "RL", savedCambers["RL"]) { onWheelSelected("RL") }
            SmallWheel("FL", selectedWheel == "FL", savedCambers["FL"]) { onWheelSelected("FL") }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            SmallWheel("RR", selectedWheel == "RR", savedCambers["RR"]) { onWheelSelected("RR") }
            SmallWheel("FR", selectedWheel == "FR", savedCambers["FR"]) { onWheelSelected("FR") }
        }
    }
}
