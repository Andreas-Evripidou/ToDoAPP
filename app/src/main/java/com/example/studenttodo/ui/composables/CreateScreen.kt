package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var taskname by remember { mutableStateOf("") }
        var taskdescription by remember { mutableStateOf("") }
        var locationradius by remember { mutableStateOf("") }
        Text("Task Name")
        OutlinedTextField(
            value = taskname,
            onValueChange = { taskname = it },
            label = { Text("task title") })

        Text("Task Description")
        OutlinedTextField(
            value = taskdescription,
            onValueChange = { taskdescription = it },
            label = { Text("task description") })

        val choices = listOf("low", "medium", "high")
        val (priority, onSelected) = remember { mutableStateOf(choices[0]) }
        Text("Task Priority")
        choices.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                choices.forEach { text ->
                    RadioButton(
                        selected = (text == priority),
                        onClick = { onSelected(text) }
                    )
                    Text(text)
                }
            }

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

            Spacer(modifier = Modifier.size(10.dp))

            Text("Location Radius:")
            TextField(
                value = locationradius,
                onValueChange = { locationradius = it },
                label = { Text("location radius") })

            Spacer(modifier = Modifier.size(5.dp))
        }

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            SubmitButton({
                var lDate = LocalDate.now()
                var lTime = LocalTime.now()
                if (reminderdate.isNotEmpty() && remindertime.isNotEmpty()) {

                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                    lDate = LocalDate.parse(reminderdate, dateFormatter)
                    lTime = LocalTime.parse(remindertime, timeFormatter)
                } else {

                }

        var date by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("") }

        Text("Reminder Date")
        var day by remember { mutableStateOf("") }
        var month by remember { mutableStateOf("") }
        var year by remember { mutableStateOf("") }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.width(70.dp),
                value = day,
                onValueChange = { textInput ->

                    day = textInput.take(2)

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

                    month =textInput.take(2)

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
                    year =textInput.take(4)

                },
                label = { Text("YYYY") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        date = day.plus("/").plus(month).plus("/").plus(year)
        Spacer(modifier = Modifier.height(16.dp))

        var enteredHours by remember { mutableStateOf("") }
        var enteredMinutes by remember { mutableStateOf("") }
        Text("Reminder Time")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hours Text Field
            OutlinedTextField(
                modifier = Modifier.width(70.dp),
                value = enteredHours,
                onValueChange = { newInput ->

                    if (newInput.toIntOrNull() in 0..24) {
                        enteredHours = newInput.take(2)
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

                    if (newInput.toIntOrNull() in 0..59) {
                        enteredMinutes = newInput.take(2)
                    }
                },
                label = { Text("MM") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        time = time.plus(enteredHours).plus(":").plus(enteredMinutes)

        Text("Take Picture (Not taught yet)")


        Text("Pick Location (Not taught yet")

        Text("Location Radius")
        OutlinedTextField(
            value = locationradius,
            onValueChange = { locationradius = it },
            label = { Text("location radius") })

        var showError by remember { mutableStateOf(false) }
        var showDateError by remember { mutableStateOf(false) }
        SubmitButton {
            var lDate = LocalDate.now()
            var lTime = LocalTime.now()


            if (date.isNotEmpty() && time.isNotEmpty() && taskname.isNotEmpty() && taskdescription.isNotEmpty() && locationradius.isNotEmpty()) {

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                try {
                    lDate = LocalDate.parse(date, dateFormatter)
                    lTime = LocalTime.parse(time, timeFormatter)

                } catch (e: DateTimeParseException) {

                    showDateError = true
                }



                val todo = ToDoEntity(
                    title = taskname,
                    reminderDate = lDate,
                    reminderTime = lTime,
                    priority = choices.indexOf(priority)+1,
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
        if(showDateError){
            displayDateTimeError()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDemo() {

}

    @Composable
    fun displayError(){
        Text("Please fill in all fields", color = MaterialTheme.colorScheme.error)
    }
    @Composable
    fun displayDateTimeError(){
        Text("Please enter a valid date and time in the correct format", color = MaterialTheme.colorScheme.error)
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDemo() {

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