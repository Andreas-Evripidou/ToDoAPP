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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen() {
    val viewModel = viewModel<CreateViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
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





        var reminderdate by remember { mutableStateOf("") }
        var remindertime by remember { mutableStateOf("") }
        Text("Reminder Date")
        TextField(
            value = reminderdate,
            onValueChange = { reminderdate = it },
            label = { Text("in format DD/MM/YYYY") })

        Text("Reminder Time")
        TextField(
            value = remindertime,
            onValueChange = { remindertime = it },
            label = { Text("in format HH:MM") })


        Text("Take Picture (Not taught yet)")


        Text("Pick Location (Not taught yet")

        Text("Location Radius")
        TextField(
            value = locationradius,
            onValueChange = { locationradius = it },
            label = { Text("location radius") })


        SubmitButton {
            val todo = ToDoEntity(
                title = taskname,
                reminderDate = reminderdate,
                reminderTime = remindertime,
                priority = choices.indexOf(priority),
                status = 0,
                description = taskdescription,
                picture = "temp",
                latitude = "temp",
                longitude = "temp",
                range = locationradius,
                createdLatitude = "temp",
                createdLongitude = "temp",
                createdDate = "temp",
                createdTime = "temp",
                moduleCode = "temp"

            )
            viewModel.createToDo(todo)
        }
    }

}
    @Composable
    fun SubmitButton(onClick: ()-> Unit){
        Button(onClick = {onClick()}){
            Text("Submit")

        }

}