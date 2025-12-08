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
import com.example.alignment_tool.data.db.CamberMeasurement
import com.example.alignment_tool.data.db.ToeMeasurement
import com.example.alignment_tool.data.repository.CamberRepository
import com.example.alignment_tool.data.repository.ToeRepository
import com.example.alignment_tool.data.viewmodel.CamberViewModel
import com.example.alignment_tool.data.viewmodel.CamberViewModelFactory
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.data.viewmodel.ToeViewModelFactory

@Composable
fun SavedValuesScreen(
    repoToe: ToeRepository,
    repoCamber: CamberRepository
) {

    // Toe ViewModel with factory
    val toeViewModel: ToeViewModel = viewModel(factory = ToeViewModelFactory(repoToe))
    val camberViewModel: CamberViewModel = viewModel (factory = CamberViewModelFactory(repoCamber))

    // Collect flows
    val toeMeasurements by toeViewModel.allMeasurements.collectAsState(initial = emptyList())
    val camberMeasurements by camberViewModel.allMeasurements.collectAsState(initial = emptyList())

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

        // ----- Toe Measurements -----
        Text("Toe Measurements", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (toeMeasurements.isEmpty()) {
            Text("No Toe Measurements saved")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(toeMeasurements) { item ->
                    MeasurementCard(item)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ----- Camber Measurements -----
        Text("Camber Measurements", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (camberMeasurements.isEmpty()) {
            Text("No Camber Measurements saved")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(camberMeasurements) { item ->
                    CamberMeasurementCard(item)
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

@Composable
fun CamberMeasurementCard(item: CamberMeasurement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Date: ${item.date}")
            Text("FL Camber: ${item.flCamber}")
            Text("FR Camber: ${item.frCamber}")
            Text("RL Camber: ${item.rlCamber}")
            Text("RR Camber: ${item.rrCamber}")
        }
    }
}
