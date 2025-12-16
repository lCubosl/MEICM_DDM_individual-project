package com.example.alignment_tool.ui.components.wheels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------------
// SMALL WHEELS AT BOTTOM RIGHT OF THE SCREEN @COMPOSABLE
// ----------------------------------------------------------
@Composable
fun SmallWheel(
    label: String,
    isSelected: Boolean,
    savedCamber: Float?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 50.dp)
            .background(
                if (isSelected) Color(0xFF90CAF9) else Color.LightGray,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                rotationZ = 90f   // rotate both texts together
            }
        ) {
            // Wheel label (FL, FR, RL, RR)
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp
            )

            // Camber value
            if (savedCamber != null) {
                Text(
                    text = "%.1f°".format(savedCamber),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}