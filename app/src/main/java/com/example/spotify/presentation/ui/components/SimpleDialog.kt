package com.example.spotify.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.example.spotify.models.presentation.DialogState

@Composable
fun SimpleDialog(data: DialogState.Simple) {
    AlertDialog(
        title = { Text(text = data.title) },
        text = data.text?.let { { Text(text = it) } },
        onDismissRequest = data.onDismiss,
        confirmButton = {
            TextButton(onClick = data.onPositive.action) {
                Text(text = data.onPositive.title)
            }
        },
        dismissButton = data.onNegative?.let { onNegative ->
            {
                TextButton(onClick = { onNegative.action }) {
                    Text(text = onNegative.title)
                }
            }
        },
        properties = DialogProperties(dismissOnClickOutside = data.dismissOnClickOutside)
    )
}