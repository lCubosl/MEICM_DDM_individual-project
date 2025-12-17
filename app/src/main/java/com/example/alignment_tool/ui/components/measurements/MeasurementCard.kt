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
import com.example.alignment_tool.data.db.ToeMeasurement
import com.example.alignment_tool.util.toReadableDate

@Composable
fun MeasurementCard(item: ToeMeasurement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Date: ${item.date.toReadableDate()}")
            Text("FL: ${item.flAngle}")
            Text("FR: ${item.frAngle}")
            Text("RL: ${item.rlAngle}")
            Text("RR: ${item.rrAngle}")
            Text("Front Toe: ${item.fToe}")
            Text("Rear Toe: ${item.rToe}")
        }
    }
}