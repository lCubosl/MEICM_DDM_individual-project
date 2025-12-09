package com.example.alignment_tool.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alignment_tool.data.viewmodel.CarViewModel
import com.example.alignment_tool.data.model.ThemeOption

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

@Composable
fun SettingsScreen(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    carViewModel: CarViewModel
) {
    val makes by carViewModel.makes.collectAsState(initial = emptyList())
    val models by carViewModel.models.collectAsState(initial = emptyList())

    var expandedMake by remember { mutableStateOf(false) }
    var expandedModel by remember { mutableStateOf(false) }

    val selectedMake by carViewModel.selectedMake.collectAsState()
    val selectedModel by carViewModel.selectedModel.collectAsState()

    LaunchedEffect(Unit) {
        carViewModel.loadMakes()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Select Car", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        // ---- Make Dropdown
        Box {
            OutlinedButton(onClick = { expandedMake = true }) {
                Text(selectedMake?.name ?: "Select Make")
            }
            DropdownMenu(expanded = expandedMake, onDismissRequest = { expandedMake = false }) {
                makes.forEach { make ->
                    DropdownMenuItem(
                        text = { Text(make.name) },
                        onClick = {
                            carViewModel.selectMake(make)
                            expandedMake = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ---- Model Dropdown
        Box {
            OutlinedButton(
                onClick = { if (models.isNotEmpty()) expandedModel = true },
                enabled = selectedMake != null
            ) {
                Text(selectedModel?.name ?: "Select Model")
            }
            DropdownMenu(expanded = expandedModel, onDismissRequest = { expandedModel = false }) {
                models.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(model.name) },
                        onClick = {
                            carViewModel.selectModel(model)
                            expandedModel = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { carViewModel.saveCarSelection() },
            enabled = selectedMake != null && selectedModel != null
        ) {
            Text("Save Car")
        }

        Spacer(Modifier.height(32.dp))

        Text("Select your theme:", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))

        ThemeSegmentedControl(
            selected = currentTheme,
            onSelect = onThemeSelected
        )
    }
}

@Composable
fun ThemeSegmentedControl(
    selected: AppTheme,
    onSelect: (AppTheme) -> Unit
) {
    val options = listOf(
        AppTheme.LIGHT to "Light",
        AppTheme.DARK to "Dark",
        AppTheme.SYSTEM to "System"
    )

    val selectedIndex = options.indexOfFirst { it.first == selected }

    var segmentWidthDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val highlightFraction = 0.9f

    val segmentWidth = segmentWidthDp
    val highlightWidth = segmentWidth * highlightFraction
    val segmentStart = segmentWidth * selectedIndex
    val targetOffset = segmentStart + (segmentWidth - highlightWidth) / 2f

    val animatedOffset by animateDpAsState(
        targetValue = targetOffset,
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Highlight behind selected segment
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(3.dp)
                .offset(x = animatedOffset)
                .width(highlightWidth)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primary)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { layout ->
                    val totalPx = layout.size.width
                    val oneSegmentPx = totalPx / options.size
                    segmentWidthDp = with(density) { oneSegmentPx.toDp() }
                }
        ) {
            options.forEachIndexed { index, (theme, label) ->

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onSelect(theme) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (index == selectedIndex)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
