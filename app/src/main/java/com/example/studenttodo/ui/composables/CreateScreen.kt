package com.example.studenttodo.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import com.example.studenttodo.viewmodels.HomeViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen() {
    val context = LocalContext.current
    val viewModel = viewModel<CreateViewModel>()
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

        Text("Reminder Time")
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("in format HH:MM") })


        Text("Take Picture (Not taught yet)")


        Text("Pick Location (Not taught yet")

        Text("Location Radius")
        OutlinedTextField(
            value = locationradius,
            onValueChange = { locationradius = it },
            label = { Text("location radius") })

        var showError by remember { mutableStateOf(false) }
        var showDateError by remember { mutableStateOf(false) }


        val moduleScope = rememberCoroutineScope()
        val modules by viewModel.modules.collectAsState(initial = emptyList())
        if (modules.size != 0) {
            val moduleTitles = makeArrayOfModuleCodes(modules)

            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf(moduleTitles[0]) }
            Text(text = "Module Code")
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
                TextField(value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                    moduleTitles.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT)
                            })

                    }
                }
            }
        }
        
        SubmitButton {
            var lDate = LocalDate.now()
            var lTime = LocalTime.now()


            if (date.isNotEmpty() && time.isNotEmpty() && taskname.isNotEmpty() && taskdescription.isNotEmpty() && locationradius.isNotEmpty()) {

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                try {
                    lDate = LocalDate.parse(date, dateFormatter)

                } catch (e: DateTimeParseException) {
                    // Error handling: Print an error message or take appropriate action

                    showDateError = true
                }

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

    @Composable
    fun SubmitButton(onClick: ()-> Unit){
        Button(onClick = {onClick()}){
            Text("Submit")

        }

}

fun makeArrayOfModuleCodes(modules: List<ModuleEntity>) : ArrayList<String> {
    var moduleCodes = ArrayList<String>()
    for (module in modules) {
        moduleCodes.add(module.moduleCode)
    }

    return moduleCodes
}