package com.example.alignment_tool.ui.components.bubble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

// ----------------------------------------------------------
//  BUBBLE CENTER OVERLAY
// ----------------------------------------------------------
@Composable
fun BubbleCenterOverlay(modifier: Modifier = Modifier, bubbleOffsetX: Float, bubbleOffsetY: Float, centeredThreshold: Float = 0.10f) {
    val distance = sqrt(bubbleOffsetX * bubbleOffsetX + bubbleOffsetY * bubbleOffsetY)
    val centered = distance < centeredThreshold

    if (!centered) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Wait for the Level Bubble to be near center before clicking \"SET\" the toe value Button.",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier.padding(34.dp)
            )
        }
    }
}
