package com.example.studenttodo.ui.composables


import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(updateSelectedScreen: (screenID: Int, newTitle: String) -> Unit) {
    val viewModel = viewModel<CreateViewModel>()
    var taskname by remember { mutableStateOf("") }
    var taskdescription by remember { mutableStateOf("") }
    var locationradius by remember { mutableStateOf("") }
    val choices = listOf("low", "medium", "high")
    val (priority, onSelected) = remember { mutableStateOf(choices[0]) }
    var reminderdate by remember { mutableStateOf("") }
    var remindertime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Task Name:")
            TextField(
                value = taskname,
                onValueChange = { taskname = it },
                label = { Text("task title") })

            Spacer(modifier = Modifier.size(10.dp))

            Text("Task Description:")
            TextField(
                value = taskdescription,
                onValueChange = { taskdescription = it },
                label = { Text("task description") })

            Spacer(modifier = Modifier.size(10.dp))

            Text("Task Priority:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                choices.forEach { text ->
                    RadioButton(
                        selected = (text == priority),
                        onClick = { onSelected(text) }
                    )
                    Text(text)
                }
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text("Reminder Date")
            TextField(
                value = reminderdate,
                onValueChange = { reminderdate = it },
                label = { Text("in format DD/MM/YYYY") })

            Spacer(modifier = Modifier.size(10.dp))

            Text("Reminder Time")
            TextField(
                value = remindertime,
                onValueChange = { remindertime = it },
                label = { Text("in format HH:MM") })


            Text("Take Picture (Not taught yet)")


            Text("Pick Location (Not taught yet")

            Spacer(modifier = Modifier.size(10.dp))

            Text("Location Radius:")
            TextField(
                value = locationradius,
                onValueChange = { locationradius = it },
                label = { Text("location radius") })

            Spacer(modifier = Modifier.size(5.dp))
        }

        Row (
            horizontalArrangement = Arrangement.Center){
            SubmitButton({
                val todo = ToDoEntity(
                    title = taskname,
                    reminderDate = LocalDate.now(),
                    reminderTime = LocalTime.now(),
                    priority = choices.indexOf(priority),
                    status = 0,
                    description = taskdescription,
                    picture = "temp",
                    latitude = "temp",
                    longitude = "temp",
                    range = locationradius,
                    createdLatitude = "temp",
                    createdLongitude = "temp",
                    createdDate = LocalDate.now(),
                    createdTime = LocalTime.now(),
                    moduleCode = "temp"
                )
                viewModel.createToDo(todo)
            }, updateSelected = updateSelectedScreen)
        }
    }
}
    @Composable
    fun SubmitButton(onClick: ()-> Unit, updateSelected: (screenID: Int, newTitle: String) -> Unit){
        Button(
            onClick = {
            onClick()
            updateSelected(ScreenID.HOME, navigationList[0].title)
        }){
            Text("Submit")

        }

}