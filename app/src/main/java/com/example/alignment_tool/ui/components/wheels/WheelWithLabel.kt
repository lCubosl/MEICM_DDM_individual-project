package com.example.alignment_tool.ui.components.wheels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------------
//  WHEEL BUTTON
// ----------------------------------------------------------
@Composable
fun WheelWithLabel(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val wheelBackground = if (isSelected) Color(0xFF90CAF9) else Color.LightGray
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(100.dp)
            .background(wheelBackground, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.background,
            fontSize = 20.sp
        )
    }
}