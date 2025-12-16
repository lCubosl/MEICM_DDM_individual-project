package com.example.alignment_tool.ui.components.wheels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WheelWithLabelAndValue(
    label: String,
    isSelected: Boolean,
    value: Float?,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WheelWithLabel(label = label, isSelected = isSelected, onClick = onClick)

        // Reserve fixed height for the value text
        Text(
            text = "%.2f°".format(value ?: 0f)            ,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.height(20.dp), // Reserve space so layout doesn't change
            textAlign = TextAlign.Center,
            color = Color(0xFF90CAF9).copy(alpha = if (value != null) 1f else 0f)
        )
    }
}