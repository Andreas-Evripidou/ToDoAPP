package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import com.example.studenttodo.viewmodels.HomeViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen() {
    val viewModel = viewModel<CreateViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var taskname by remember { mutableStateOf("") }
        var taskdescription by remember { mutableStateOf("") }
        var locationradius by remember { mutableStateOf("") }
        Text("Task Name")
        TextField(
            value = taskname,
            onValueChange = { taskname = it },
            label = { Text("task title") })

        Text("Task Description")
        TextField(
            value = taskdescription,
            onValueChange = { taskdescription = it },
            label = { Text("task description") })

        val choices = listOf("high", "medium", "low")
        val (priority, onSelected) = remember { mutableStateOf(choices[0]) }
        Text("Task Priority")
        choices.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (text == priority),
                    onClick = { onSelected(text) }
                )
                Text(text)
            }
        }





        var date by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("") }

        Text("Reminder Date")
        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("in format DD/MM/YYYY") })

        Text("Reminder Time")
        TextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("in format HH:MM") })


        Text("Take Picture (Not taught yet)")


        Text("Pick Location (Not taught yet")

        Text("Location Radius")
        TextField(
            value = locationradius,
            onValueChange = { locationradius = it },
            label = { Text("location radius") })

        var showError by remember { mutableStateOf(false) }
        SubmitButton {
            var lDate = LocalDate.now()
            var lTime = LocalTime.now()


            if (date.isNotEmpty() && time.isNotEmpty() && taskname.isNotEmpty() && taskdescription.isNotEmpty() && locationradius.isNotEmpty()) {
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                lDate = LocalDate.parse(date, dateFormatter)
                lTime = LocalTime.parse(time, timeFormatter)

                val todo = ToDoEntity(
                    title = taskname,
                    reminderDate = lDate,
                    reminderTime = lTime,
                    priority = choices.indexOf(priority),
                    status = 0,
                    description = taskdescription,
                    picture = "temp",
                    latitude = "temp",
                    longitude = "temp",
                    range = locationradius,
                    createdLatitude = "temp",
                    createdLongitude = "temp",
                    moduleCode = "temp"
                )
                viewModel.createToDo(todo)
            } else {
                showError = true
            }


        }
        if (showError) {
            displayError()
        }
    }

}

    @Composable
    fun displayError(){
        Text("Please fill in all fields", color = MaterialTheme.colorScheme.error)
    }

    @Composable
    fun SubmitButton(onClick: ()-> Unit){
        Button(onClick = {onClick()}){
            Text("Submit")

        }

}