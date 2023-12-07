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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(date: String, updateSelectedDate: (d: String, m: String, y: String) -> Unit){

    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    val dateParts = date.split("-")
    if (dateParts.size == 3) {
        year = dateParts[0]
        month = dateParts[1]
        day = dateParts[2]
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.width(70.dp),
            value = day,
            onValueChange = { textInput ->

                day = textInput.take(2)
                updateSelectedDate(day, month, year)

            },
            label = { Text("DD") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Text("/")

        OutlinedTextField(
            modifier = Modifier.width(70.dp),
            value = month,
            onValueChange = { textInput ->

                month = textInput.take(2)
                updateSelectedDate(day, month, year)

            },
            label = { Text("MM") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Text("/")


        OutlinedTextField(
            modifier = Modifier.width(80.dp),
            value = year,
            onValueChange = { textInput ->
                year = textInput.take(4)
                updateSelectedDate(day, month, year)

            },
            label = { Text("YYYY") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
    }
}