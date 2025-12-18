package com.example.alignment_tool.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alignment_tool.data.repository.CamberRepository
import com.example.alignment_tool.data.repository.ToeRepository
import com.example.alignment_tool.data.viewmodel.CamberViewModel
import com.example.alignment_tool.data.viewmodel.CamberViewModelFactory
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.data.viewmodel.ToeViewModelFactory
import com.example.alignment_tool.ui.components.measurements.CamberMeasurementCard
import com.example.alignment_tool.ui.components.measurements.MeasurementCard
import com.example.alignment_tool.ui.components.measurements.MeasurementsSection

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
        Text("Saved Measurements", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        MeasurementsSection(
            title = "Toe Measurements",
            measurements = toeMeasurements,
            emptyMessage = "No Toe Measurements saved",
            itemContent = { MeasurementCard(it) },
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.height(16.dp))

        MeasurementsSection(
            title = "Camber Measurements",
            measurements = camberMeasurements,
            emptyMessage = "No Camber Measurements saved",
            itemContent = { CamberMeasurementCard(it) },
            modifier = Modifier.weight(1f)
        )
    }
}