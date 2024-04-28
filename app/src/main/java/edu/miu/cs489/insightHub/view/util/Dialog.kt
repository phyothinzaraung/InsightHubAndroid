package edu.miu.cs489.insightHub.view.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun showDialog(title: String, message: String, onDimissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDimissRequest,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onDimissRequest
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun showLoadingDialog(message: String, onDimissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDimissRequest,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .size(100.dp)
                .background(White, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
}