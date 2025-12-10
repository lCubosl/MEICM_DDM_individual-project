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
    val toeViewModel: ToeViewModel = viewModel(factory = ToeViewModelFactory(repoToe))
    val camberViewModel: CamberViewModel = viewModel(factory = CamberViewModelFactory(repoCamber))

    val toeMeasurements by toeViewModel.allMeasurements.collectAsState(initial = emptyList())
    val camberMeasurements by camberViewModel.allMeasurements.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Saved Alignments", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        MeasurementsSection(
            title = "Toe Measurements",
            measurements = toeMeasurements,
            emptyMessage = "No Toe Measurements saved",
            itemContent = { MeasurementCard(it) }
        )

        Spacer(Modifier.height(16.dp))

        MeasurementsSection(
            title = "Camber Measurements",
            measurements = camberMeasurements,
            emptyMessage = "No Camber Measurements saved",
            itemContent = { CamberMeasurementCard(it) }
        )
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

@Composable
fun <T> MeasurementsSection(
    title: String,
    measurements: List<T>,
    emptyMessage: String,
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (measurements.isEmpty()) {
            Text(emptyMessage)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(measurements) { item ->
                    itemContent(item)
                }
            }
        }
    }
}
