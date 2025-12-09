package com.example.alignment_tool.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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

@Composable
fun SettingsScreen(
    currentTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit,
    carViewModel: CarViewModel
) {
    // launches CarAPI with key and loads "makes/v2"
    LaunchedEffect(Unit) {
        carViewModel.loadMakes()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(24.dp))

        // set theme buttons
        ThemeSegmentedControl(
            selected = currentTheme,
            onSelect = onThemeSelected
        )

        Spacer(Modifier.height(24.dp))

        // car make and model buttons
        CarSelector(carViewModel)
    }
}

// ----------------------------------------------------------
//  THEME BUTTONS SLEECTOR
// ----------------------------------------------------------
@Composable
fun ThemeSegmentedControl(
    selected: ThemeOption,
    onSelect: (ThemeOption) -> Unit
) {
    val options = listOf(
        ThemeOption.LIGHT to "Light",
        ThemeOption.DARK to "Dark",
        ThemeOption.SYSTEM to "System"
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

    Text("Select your theme:", fontSize = 16.sp)
    Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun CarSelector(carViewModel: CarViewModel) {
    val makes by carViewModel.makes.collectAsState()
    val models by carViewModel.models.collectAsState()
    val selectedMake by carViewModel.selectedMake.collectAsState()
    val selectedModel by carViewModel.selectedModel.collectAsState()

    var expandedMake by remember { mutableStateOf(false) }
    var expandedModel by remember { mutableStateOf(false) }

    // Title
    Text("Select your vehicle:", fontSize = 16.sp)

    Spacer(Modifier.height(16.dp))

    // Buttons Row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // MAKE BUTTON
        OutlinedButton(
            onClick = { expandedMake = true },
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(selectedMake?.name ?: "Make")
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        // MODEL BUTTON
        OutlinedButton(
            onClick = { if (models.isNotEmpty()) expandedModel = true },
            enabled = selectedMake != null,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(selectedModel?.name ?: "Model")
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        // SAVE CAR BUTTON
        Button(
            onClick = { carViewModel.saveCarSelection() },
            enabled = selectedMake != null && selectedModel != null
        ) {
            Text("Save Car")
        }
    }

    // MAKE DROPDOWN
    DropdownMenu(
        expanded = expandedMake,
        onDismissRequest = { expandedMake = false },
        modifier = Modifier
            .width(200.dp)
            .heightIn(max = 250.dp)
    ) {
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

    // MODEL DROPDOWN
    DropdownMenu(
        expanded = expandedModel,
        onDismissRequest = { expandedModel = false },
        modifier = Modifier
            .width(200.dp)
            .heightIn(max = 250.dp)
    ) {
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