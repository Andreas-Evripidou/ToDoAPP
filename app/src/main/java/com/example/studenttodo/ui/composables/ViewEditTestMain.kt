package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studenttodo.ui.composables.components.CustomDialogue

@Composable
fun MyComposeScreen() {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Show Dialog")
        }

        if (showDialog) {
            CustomDialogue(
                onDismissRequest = { showDialog = false },
                onConfirmation = { showDialog = true; /* your confirmation logic here */ },
            )
        }
    }
}

@Preview
@Composable
fun MyComposeScreenPreview() {
    MaterialTheme {
        MyComposeScreen()
    }
}