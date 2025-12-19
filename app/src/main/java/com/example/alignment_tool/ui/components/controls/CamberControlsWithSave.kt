package com.example.alignment_tool.ui.components.controls

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ----------------------------------------------------------
// OVERLAY CONFIRMATION. IF YES SAVES DATA TO LOCAL DATABASE
// TODO JOIN CAMBERCONTROLSWITH SAVE AND TOECONTROLSWITHSAVE SINCE THEY ARE THE SAME
// ----------------------------------------------------------
@Composable
fun CamberControlsWithSave(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save All Cambers?") },
        text = { Text("Are you sure that you want to save the current camber values?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No", color = Color.Black)
            }
        }
    )
}