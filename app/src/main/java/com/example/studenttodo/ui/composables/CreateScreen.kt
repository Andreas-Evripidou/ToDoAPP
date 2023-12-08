package com.example.studenttodo.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.ui.composables.components.CustomSubMenu
import com.example.studenttodo.ui.composables.components.ModuleCreateDialog
import com.example.studenttodo.ui.composables.components.SelectDate
import com.example.studenttodo.ui.composables.components.SelectImage
import com.example.studenttodo.ui.composables.components.SelectLocation
import com.example.studenttodo.ui.composables.components.SelectOrCreateModule
import com.example.studenttodo.ui.composables.components.SelectTime
import com.example.studenttodo.viewmodels.CreateViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun SubmitButton(onClick: ()-> Unit){
    Button(
        onClick = {
            onClick()

        }){
        Text("Submit")

    }

}
@Composable
fun DisplayError(){
    Text("Please fill in all required fields (with *)", color = MaterialTheme.colorScheme.error)
}
@Composable
fun DisplayDateTimeError(){
    Text("Please enter a valid date and time in the correct format", color = MaterialTheme.colorScheme.error)
}


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(toDo: ToDoEntity = viewModel<CreateViewModel>().emptyTodo(), edit: Boolean = false, onDismiss : ()->Unit = {}) {
    val viewModel = viewModel<CreateViewModel>()
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        ModuleCreateDialog(openDialog = openDialog)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var taskName by remember { mutableStateOf(toDo.title) }
        var taskDescription by remember { mutableStateOf(toDo.description) }

        Row {
            Text("Task Name:",
                style = MaterialTheme.typography.headlineSmall)
            Text(" *", color = Color.Red)
        }

        Row {
            Spacer(modifier = Modifier.weight(0.5f))
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = {

                    Text("task title")
                },
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        var moduleCode = toDo.moduleCode
        fun updateSelectedDialog (open: Boolean){
            openDialog.value = open
        }
        fun updateSelectedModuleCode(mc: String) {
            moduleCode = mc
        }

        Row {
            Text("Task Description:",
                style = MaterialTheme.typography.headlineSmall)
            Text(" *", color = Color.Red)
        }
        Row {
            Spacer(modifier = Modifier.weight(0.5f))
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("task description", style = MaterialTheme.typography.labelLarge) },
                modifier = Modifier
                    .heightIn(min = 112.dp)
                    .weight(2f))// Minimum height for the text field
            Spacer(modifier = Modifier.weight(0.5f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row{
            Spacer(modifier = Modifier.weight(0.5f))
            Box (modifier = Modifier.weight(2f)){
                SelectOrCreateModule(
                    rowModifier = Modifier.fillMaxWidth(),
                    openDialog = ::updateSelectedDialog,
                    updateSelectedModuleCode = ::updateSelectedModuleCode,
                    moduleCode = moduleCode)
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }


        Spacer(modifier = Modifier.height(16.dp))
        val choices = listOf("Low", "Medium", "High")
        val (priority, onSelected) = remember { mutableStateOf(choices[toDo.priority-1]) }
        Row {
            Text("Task Priority:",
                style = MaterialTheme.typography.headlineSmall)
            Text(" *", color = Color.Red)
        }
        var expanded by remember { mutableStateOf(false) }
        Row (
            verticalAlignment = Alignment.CenterVertically)
        {
            Spacer(modifier = Modifier.weight(0.5f))
            ExposedDropdownMenuBox(
                modifier = Modifier.weight(2f),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                TextField(
                    value = priority,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    choices.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                onSelected(item)
                                expanded = false
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }

        Spacer(modifier = Modifier.height(16.dp))


        var date by remember { mutableStateOf(toDo.reminderDate.toString()) }
        var time by remember { mutableStateOf(toDo.reminderTime.toString().substring(0, 5)) }


        val showDateTimeFields = remember { mutableStateOf(false) }
        val text = if (edit) "Edit Reminder" else "Add Reminder"

        CustomSubMenu(text, showDateTimeFields)

        if (showDateTimeFields.value) {
            fun updateSelectedDate(d: String, m: String, y: String){
                date = y.plus("-").plus(m).plus("-").plus(d)
            }
            SelectDate(date = date, updateSelectedDate = ::updateSelectedDate)


            Spacer(modifier = Modifier.height(16.dp))

            fun updateSelectedTime(h: String, m: String){
                time = h.plus(":").plus(m)
            }
            SelectTime(time =  time, updatedSelectedTime = ::updateSelectedTime )


        }
        var longitude by remember { mutableStateOf(toDo.longitude) }
        var latitude by remember { mutableStateOf(toDo.latitude) }
        var locationRadius by remember { mutableStateOf(toDo.range) }

        fun updatedSelectedLocation(lon: String, lat: String, radius: String){
            longitude = lon
            latitude = lat
            locationRadius = radius
        }

        SelectLocation(lon = longitude,
            lat = latitude,
            radius = locationRadius,
            edit = edit,
            updateSelectedLoc = ::updatedSelectedLocation)


        var selectedUri by remember { mutableStateOf(toDo.picture) }
        fun updateSelectedUri (uri: String){
            selectedUri = uri
        }

        SelectImage(selectedUri, ::updateSelectedUri, edit = edit)

        Spacer(modifier = Modifier.height(16.dp))

        var showError by remember { mutableStateOf(false) }
        var showDateError by remember { mutableStateOf(false) }
        var showSuccess by remember { mutableStateOf(false) }
        Row (horizontalArrangement = Arrangement.SpaceEvenly){
            if (edit) {
                Button(onClick = { onDismiss() }) {
                    Text(text = "Dismiss")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            SubmitButton {
                var lDate = LocalDate.now()
                var lTime = LocalTime.now()


                if (taskName.isNotEmpty() && taskDescription.isNotEmpty()) {

                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    try {
                        lDate = LocalDate.parse(date, dateFormatter)
                        lTime = LocalTime.parse(time, timeFormatter)

                    } catch (e: DateTimeParseException) {
                        showDateError = true
                    }


                    val todo = ToDoEntity(
                        id = toDo.id,
                        title = taskName,
                        reminderDate = lDate,
                        reminderTime = lTime,
                        priority = choices.indexOf(priority) + 1,
                        status = toDo.status,
                        description = taskDescription,
                        picture = selectedUri,
                        latitude = latitude,
                        longitude = longitude,
                        range = locationRadius,
                        createdLatitude = "temp",
                        createdLongitude = "temp",
                        moduleCode = moduleCode
                    )
                    viewModel.createOrUpdateToDo(todo, edit)
                    showSuccess = true
                } else {
                    showError = true
                }


            }
        }

        if (showError) {
            DisplayError()
        }
        if (showDateError) {
            DisplayDateTimeError()
        }
        if (showSuccess) {
            onDismiss()
        }
    }


}




