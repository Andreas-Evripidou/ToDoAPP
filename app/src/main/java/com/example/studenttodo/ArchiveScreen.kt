package com.example.studenttodo

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun ArchiveScreen(){
    Column {

        var bold by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Bold")
            Spacer(modifier = Modifier.size(8.dp))
            Switch(checked = bold, onCheckedChange = { bold = it })
        }
        Spacer(modifier = Modifier.size(38.dp))
        val choices = listOf("small", "medium", "large", "extra large")
        val (size, onSelected) = remember { mutableStateOf(choices[1]) }
        Text(text = "Size")
        choices.forEach { text ->
            Row (verticalAlignment = Alignment.CenterVertically){
                RadioButton(selected = (text == size), onClick = { onSelected(text) })
                Text(text)
            }
        }
        Spacer(modifier = Modifier.size(38.dp))

        val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        var selectedDays by remember { mutableStateOf(BooleanArray(weekdays.size) {false}) }
        weekdays.forEachIndexed { index,weekday ->
            var thisDay by remember{ mutableStateOf(selectedDays[index]) }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(weekday)
                Checkbox(
                    checked = thisDay,
                    onCheckedChange = {
                        thisDay = !thisDay
                        selectedDays[index] = thisDay
                    } )
            }

        }
        Button({ Log.i("LogLog", selectedDays.contentToString())}){
            Text(text = "Log")
        }

    }
}