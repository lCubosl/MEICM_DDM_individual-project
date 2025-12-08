package com.example.alignment_tool.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

@Composable
fun SettingsScreen(currentTheme: AppTheme, onThemeSelected: (AppTheme) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
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

    val highlightFraction = 0.9f   // any value you want

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
            .fillMaxWidth(1f)
            .height(48.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Sliding highlight
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