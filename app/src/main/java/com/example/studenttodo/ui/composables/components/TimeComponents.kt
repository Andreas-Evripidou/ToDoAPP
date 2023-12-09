package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/*
* Composable function to display
* Time options
* */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTime(time: String, updatedSelectedTime: (h: String, m: String) -> Unit){

    var enteredHours by remember { mutableStateOf("") }
    var enteredMinutes by remember { mutableStateOf("") }

    val timeParts = time.split(":")
    if (timeParts.size == 2) {
        enteredHours = timeParts[0]
        enteredMinutes = timeParts[1]
    }


    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hours Text Field
        OutlinedTextField(
            modifier = Modifier.width(70.dp),
            value = enteredHours,
            onValueChange = { newInput ->

                if (newInput.toIntOrNull() in 0..24 || newInput == "") {
                    enteredHours = newInput.take(2)
                    updatedSelectedTime(enteredHours, enteredMinutes)
                }
            },
            label = { Text("HH") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Text(":")


        OutlinedTextField(
            modifier = Modifier.width(70.dp),
            value = enteredMinutes,
            onValueChange = { newInput ->

                if (newInput.toIntOrNull() in 0..59 || newInput == "") {
                    enteredMinutes = newInput.take(2)
                    updatedSelectedTime(enteredHours, enteredMinutes)
                }
            },
            label = { Text("MM") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
    }

}
