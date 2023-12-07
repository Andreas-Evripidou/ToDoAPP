package com.example.studenttodo.ui.composables

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
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
fun displayError(){
    Text("Please fill in all required fields (with *)", color = MaterialTheme.colorScheme.error)
}
@Composable
fun displayDateTimeError(){
    Text("Please enter a valid date and time in the correct format", color = MaterialTheme.colorScheme.error)
}

@Composable
fun displaySuccess(){
    Text(
        text = "ToDo Successfully Added",
        color = Color.Green,

    )
}


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(toDo: ToDoEntity = viewModel<CreateViewModel>().emptyTodo(), edit: Boolean = false, onDismiss : ()->Unit = {}) {
    val viewModel = viewModel<CreateViewModel>()
    var openDialog = remember { mutableStateOf(false) }

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
            Text("Task Name")
            Text(
                " *",
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = {

                Text("task title")
            })
        Spacer(modifier = Modifier.height(16.dp))

        var moduleCode = toDo.moduleCode
        fun updateSelectedDialog (open: Boolean){
            openDialog.value = open
        }
        fun updateSelectedModuleCode(mc: String) {
            moduleCode = mc
        }
        SelectOrCreateModule(
            rowModifier = Modifier.width(250.dp),
            openDialog = ::updateSelectedDialog,
            updateSelectedModuleCode = ::updateSelectedModuleCode,
            moduleCode = moduleCode
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("Task Description")
            Text(
                " *",
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("task description") })

        Spacer(modifier = Modifier.height(16.dp))
        val choices = listOf("low", "medium", "high")
        val (priority, onSelected) = remember { mutableStateOf(choices[toDo.priority-1]) }
        Row {
            Text("Task Priority")
            Text(
                " *",
                color = Color.Red
            )
        }
        choices.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (text == priority),
                    onClick = { onSelected(text) }
                )
                Text(text)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))


        var date by remember { mutableStateOf(toDo.reminderDate.toString()) }
        var time by remember { mutableStateOf(toDo.reminderTime.toString()) }


        var showDateTimeFields by remember { mutableStateOf(false) }
        val text = if (edit) "Edit Reminder" else "Add Reminder"

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showDateTimeFields = !showDateTimeFields }
        ) {
            Checkbox(
                checked = showDateTimeFields,
                onCheckedChange = { showDateTimeFields = it })
            Text(text)
        }

        if (showDateTimeFields) {
            fun updateSelectedDate(d: String, m: String, y: String){
                date = d.plus("/").plus(m).plus("/").plus(y)
            }
            SelectDate(date = date, updateSelectedDate = ::updateSelectedDate)


            Spacer(modifier = Modifier.height(16.dp))

            fun updateSelectedTime(h: String, m: String){
                time = time.plus(h).plus(":").plus(m)
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

                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")
                    try {
                        lDate = LocalDate.parse(date, dateFormatter)
                        lTime = LocalTime.parse(time, timeFormatter)

                    } catch (e: DateTimeParseException) {
                        // Error handling: Print an error message or take appropriate action
                        Log.i("Wrong date: $date or time: $time", "datetimerror")
                    }


                    val todo = ToDoEntity(
                        id = toDo.id,
                        title = taskName,
                        reminderDate = lDate,
                        reminderTime = lTime,
                        priority = choices.indexOf(priority) + 1,
                        status = 0,
                        description = taskDescription,
                        picture = selectedUri,
                        latitude = latitude,
                        longitude = longitude,
                        range = locationRadius,
                        createdLatitude = "temp",
                        createdLongitude = "temp",
                        moduleCode = moduleCode
                    )
                    viewModel.createOrUpdateToDo(todo)
                    showSuccess = true
                } else {
                    showError = true
                }


            }
        }

        if (showError) {
            displayError()
        }
        if (showDateError) {
            displayDateTimeError()
        }
        if (showSuccess) {
            displaySuccess()
        }
    }


}




