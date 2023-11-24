package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studenttodo.ui.composables.SubmitButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
                onConfirmation = { showDialog = false;},
            )
        }
    }
}

@Composable
fun CustomDialogue(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            var taskDateTime = LocalDateTime.now().format(formatter)
            var distanceRadiusPlaceholder = "1.5km"
            Row(
                Modifier.fillMaxWidth().padding(top = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = "Date/Time: "
                )
                Text(
                    modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                    text = taskDateTime
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = "Location: "
                )

                Text(
                    text = "Location Placeholder",
                    modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                    fontSize = 24.sp
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = "Distance Radius: "
                )

                Text(
                    modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                    text = distanceRadiusPlaceholder,
                    fontSize = 16.sp
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 50.dp, bottom = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = "Picture: "
                )

                Text(
                    text = "Picture Placeholder",
                    modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                    fontSize = 24.sp
                )
            }

            SubmitButton() {
            }

        }
        @Composable
        fun SubmitButton(onClick: () -> Unit) {
            Button(onClick = { onClick() }) {
                Text("Submit")
            }
        }
    }
}