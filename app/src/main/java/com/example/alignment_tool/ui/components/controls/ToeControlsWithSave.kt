package com.example.alignment_tool.ui.components.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ----------------------------------------------------------
//  TOE BUTTONS
// ----------------------------------------------------------
@Composable
fun ToeControlsWithSave(
    isSetEnabled: Boolean,
    allWheelsSet: Boolean,
    onSet: () -> Unit,
    onSave: () -> Unit,
    onRestart: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (allWheelsSet) {
            // Row with SET + SAVE buttons
            Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // SET button - 2/3 width
                Button(
                    onClick = onSet,
                    enabled = isSetEnabled,
                    modifier = Modifier.weight(2f).height(56.dp)
                ) {
                    Text("SET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // SAVE button - 1/3 width, green
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("SAVE", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Only SET button
            Button(
                onClick = onSet,
                enabled = isSetEnabled,
                modifier = Modifier.fillMaxWidth(0.8f).height(56.dp)
            ) {
                Text("SET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Restart button below
        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth(0.8f).height(56.dp)
        ) {
            Text("Restart", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Save All Toe Values?") },
            text = { Text("Are you sure you want to save all current toe measurements?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSave()  // call the save logic
                        showConfirmDialog = false
                    }
                ) {
                    Text("Yes", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No", color = Color.Black)
                }
            }
        )
    }
}