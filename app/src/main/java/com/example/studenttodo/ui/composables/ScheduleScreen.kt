package com.example.studenttodo.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.viewmodels.ScheduleViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun ScheduleScreen (name: String, modifier: Modifier = Modifier) {
    val times by viewModel<ScheduleViewModel>().timetable.collectAsState(initial = emptyList())
    val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top

        ) {
        //This is the day of the week, repeat for each work day of the week
            weekdays.forEach { weekday ->
                Text(
                    text = weekday,
                    modifier = modifier,
                    style = MaterialTheme.typography.headlineSmall
                )
                //This repeats for each data value matching the current day of the week
                val current = times.filter {it.day == weekday}
                current.forEach { time ->
                    CreateButtons(
                        time = time)}

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val openDialog = remember { mutableStateOf(false)  }
                    if (openDialog.value) {
                        DialogAdd( openDialog, weekday)
                    }
                    Box(modifier = Modifier
                        .width(50.dp)
                        .clickable { openDialog.value = true }
                    ) {
                        Icon(
                            Icons.Filled.AddCircle,
                            contentDescription = "Add"
                        )
                    }
                }

                }
        }
}

//Make the module code a drop down and link the Module Title
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAdd( openDialog: MutableState<Boolean>, weekday: String){
    val viewModel = viewModel<ScheduleViewModel>()
    var moduleCode by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }

    AlertDialog(
        title = {Text(text = "Add a Day to Your Schedule")},
        text = {Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ){
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Module Code:")
                TextField(
                    value = moduleCode,
                    onValueChange = { moduleCode = it },
                    label = { Text("Module Code") })

                Spacer(modifier = Modifier.size(10.dp))

                Text("Start Time:")
                TextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time in HH:mm format") })

                Spacer(modifier = Modifier.size(10.dp))

                Text("End Time:")
                TextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End Time in HH:mm format") })

                Spacer(modifier = Modifier.size(10.dp))
            }

        }},
        onDismissRequest = {openDialog.value = false},
        dismissButton = {
            Button(onClick = { openDialog.value = false})
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick =
            {if (weekday.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && moduleCode.isNotEmpty()) {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                try {
                    val newStartTime = LocalTime.parse(startTime, timeFormatter)
                    val newEndTime = LocalTime.parse(endTime, timeFormatter)
                    val entry = TimetableEntity(
                        day = weekday,
                        startTime = newStartTime,
                        endTime = newEndTime,
                        moduleCode = moduleCode)
                    viewModel.createTimetable(entry)
                    openDialog.value = false

                }
                catch(e: DateTimeParseException){
                    showDateError = true
                }

            }
            else {
                showError = true
            }
            }
                )
            {
                Text(text = "Add")
            }
            if (showError) {
                displayError1()
            }
            if(showDateError){
                displayDateTimeError2()
            }
        }
    )
}


@Composable
fun displayError1(){
    Text("Please fill in all fields", color = MaterialTheme.colorScheme.error)
}
@Composable
fun displayDateTimeError2(){
    Text("Please enter a valid date and time in the correct format", color = MaterialTheme.colorScheme.error)
}

//Make the module code a drop down and link the Module Title
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEdit( openDialog: MutableState<Boolean>, time: TimetableEntity){
    val viewModel = viewModel<ScheduleViewModel>()
    var moduleCode by remember { mutableStateOf(time.moduleCode) }
    var startTime by remember { mutableStateOf(time.startTime.toString()) }
    var endTime by remember { mutableStateOf(time.endTime.toString()) }
    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }
    AlertDialog(
        title = {Text(text = "Add a Day to Your Schedule")},
        text = {Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ){
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Module Code:")
                TextField(
                    value = moduleCode,
                    onValueChange = { moduleCode = it },
                    label = { Text("Module Code") })

                Spacer(modifier = Modifier.size(10.dp))

                Text("Start Time:")
                TextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time in HH:mm format") })

                Spacer(modifier = Modifier.size(10.dp))

                Text("End Time:")
                TextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End Time in HH:mm format") })
                Spacer(modifier = Modifier.size(10.dp))
            }
        }},
        onDismissRequest = {openDialog.value = false},
        dismissButton = {
            Button(onClick = { openDialog.value = false})
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick =
            {
                if (startTime.isNotEmpty() && endTime.isNotEmpty() && moduleCode.isNotEmpty()) {
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    try {
                        val newStartTime = LocalTime.parse(startTime, timeFormatter)
                        val newEndTime = LocalTime.parse(endTime, timeFormatter)
                        val entry = TimetableEntity(
                            id = time.id,
                            day = time.day,
                            startTime = newStartTime,
                            endTime = newEndTime,
                            moduleCode = moduleCode)
                        viewModel.editTimetable(entry)
                        openDialog.value = false
                    
                    }
                    catch(e: DateTimeParseException){
                        showDateError = true
                    }

                }
                else {
                    showError = true
                }
            })
            {
                Text(text = "Submit Changes")
            }
            if (showError) {
                displayError1()
            }
            if(showDateError){
                displayDateTimeError2()
            }
        }
    )
}


@Composable
fun DialogView(openDialog: MutableState<Boolean>, openDialogEdit: MutableState<Boolean>, time: TimetableEntity ){
    AlertDialog(
        title = {Text(text = "Lecture")},
        text = {Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Module Code: \n ${time.moduleCode} ",
                modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Time: \n ${time.startTime} - ${time.endTime}",
                modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall)

        }}
        ,
        onDismissRequest = {openDialog.value = false},
        dismissButton = {
            Button(onClick = { openDialog.value = false})
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick =
            {
                openDialogEdit.value = true //Bring the user to a part that allows them to edit the details
                openDialog.value = false
            }
            )
            {
                Text(text = "Edit")
            }
        }
    )
}

//This is used to delete a timetable entry from the database, when sizyk merges with master
// update it so it only changes status
@Composable
fun DialogDelete( openDialog: MutableState<Boolean>, time: TimetableEntity){
    val viewModel = viewModel<ScheduleViewModel>()
    AlertDialog(
        title = {Text(text = "Are you sure you want to delete the following lecture from your schedule?")},
        text = {Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Module Code: \n ${time.moduleCode} ",
                modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Time: \n ${time.startTime} - ${time.endTime}",
                modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall)

        }}
        ,
        onDismissRequest = {openDialog.value = false},
        dismissButton = {
            Button(onClick = { openDialog.value = false })
            {
                Text(text = "Dismiss")
            }
        },

        confirmButton = {
            Button(onClick =
            {
                //Currently deletes the timetable of it outright
                viewModel.deleteTimetable(time)
                openDialog.value = false
            }
            )
            {
                Text(text = "Delete")
            }
        }

    )
}

@Composable
fun CreateButtons(
    time: TimetableEntity) {
    val openDialogView = remember { mutableStateOf(false)  }
    val openDialogDelete = remember { mutableStateOf(false)  }
    val openDialogEdit = remember{ mutableStateOf(false)  }
    if (openDialogView.value) {
        DialogView(openDialogView,openDialogEdit, time) //This brings up the view alert
    }
    if (openDialogDelete.value) {
        DialogDelete(openDialogDelete, time) //This brings up the delete alert
    }
    if (openDialogEdit.value) {
        DialogEdit( openDialog = openDialogEdit, time = time) //This brings up the edit alert
    }
    Card(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .height(110.dp)
            .padding(12.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(90.dp)
        ) {

            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { openDialogView.value = true } //This is to let the user view the entire module data
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(text = time.moduleCode, style = MaterialTheme.typography.headlineSmall) //This is going to be the moduleName
                    Text(text = "Module code: ${time.moduleCode}")
                    Text(text = "Time: ${time.startTime} - ${time.endTime}")

                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Box( modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .clickable { openDialogDelete.value = true }// This will remove the module from the user's view
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", Modifier.fillMaxSize())
            }
        }
    }

}

