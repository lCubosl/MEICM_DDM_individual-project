package com.example.alignment_tool.ui.components.measurements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alignment_tool.data.db.CamberMeasurement
import com.example.alignment_tool.util.toReadableDate

@Composable
fun CamberMeasurementCard(item: CamberMeasurement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Date: ${item.date.toReadableDate()}")
            Text("FL Camber: ${item.flCamber}")
            Text("FR Camber: ${item.frCamber}")
            Text("RL Camber: ${item.rlCamber}")
            Text("RR Camber: ${item.rrCamber}")
        }
    }
}