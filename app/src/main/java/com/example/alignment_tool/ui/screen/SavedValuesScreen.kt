package com.example.alignment_tool.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alignment_tool.data.db.ToeMeasurement
import com.example.alignment_tool.data.repository.ToeRepository
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.data.viewmodel.ToeViewModelFactory
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SavedValuesScreen(repo: ToeRepository) {

    // Get ViewModel WITH factory (needed because your ViewModel has constructor args)
    val viewModel: ToeViewModel = viewModel(factory = ToeViewModelFactory(repo))

    // Collect the Flow<List<ToeMeasurement>>
    val measurements by viewModel.allMeasurements.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Saved Alignments",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        if (measurements.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No Saved Measurements")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(measurements) { item ->
                    MeasurementCard(item)
                }
            }
        }
    }
}

@Composable
fun MeasurementCard(item: ToeMeasurement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Date: ${item.date}")
            Text("FL: ${item.flAngle}")
            Text("FR: ${item.frAngle}")
            Text("RL: ${item.rlAngle}")
            Text("RR: ${item.rrAngle}")
            Text("Front Toe: ${item.fToe}")
            Text("Rear Toe: ${item.rToe}")
        }
    }
}