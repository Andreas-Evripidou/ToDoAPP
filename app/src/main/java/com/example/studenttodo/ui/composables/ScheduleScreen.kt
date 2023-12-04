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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import com.example.studenttodo.viewmodels.ScheduleViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun ScheduleScreen (name: String, modifier: Modifier = Modifier) {
    val times by viewModel<ScheduleViewModel>().timetable.collectAsState(initial = emptyList())
    val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

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
            if (time.status ==0){ CreateButtons(time = time)}}
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
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ModuleCreateDialog(openDialog: MutableState<Boolean>, openDialog2: MutableState<Boolean>) {
    val viewModel = viewModel<CreateViewModel>()
    var code by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var long by remember { mutableStateOf("") }
    var moduleTitle by remember { mutableStateOf("") }
    AlertDialog(
        title = { Text(text = "Create Module",modifier = Modifier,
            style = MaterialTheme.typography.headlineMedium )},
        text = {
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Module Title",modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = moduleTitle,
                    onValueChange = { moduleTitle = it },
                    label = { Text(text = "Module Title") })
                Spacer(modifier = Modifier.size(10.dp))

                Text(text = "Module Code", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text(text = "Module Code") })
                Spacer(modifier = Modifier.size(10.dp))

                Text(text = "Latitude", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = lat,
                    onValueChange = { lat = it },
                    label = { Text(text = "Latitude") })
                Spacer(modifier = Modifier.size(10.dp))

                Text(text = "Longitude", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = long,
                    onValueChange = { long = it },
                    label = { Text(text = "Longitude") })
                Spacer(modifier = Modifier.size(10.dp))
            }
        },
        onDismissRequest = { openDialog.value = false
            openDialog2.value = true },
        dismissButton = {
            Button(onClick = { openDialog.value = false
                openDialog2.value = true})
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick = {
                val module = ModuleEntity(
                    moduleCode = code,
                    lat = lat,
                    long = long,
                    moduleTitle = moduleTitle
                )
                viewModel.createModule(module = module)
                openDialog.value = false
                openDialog2.value = true
            }) {
                Text(text = "Create Module")
            }
        })
}

fun findModule(modules: List<ModuleEntity>, currentModuleCode: String): ModuleEntity {
    var requiredModule = modules[0]
    for (module in modules) {
        if (module.moduleCode == currentModuleCode) {
            requiredModule = module
        }
    }

    return requiredModule
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAdd( openDialog: MutableState<Boolean>, weekday: String){
    val viewModel = viewModel<ScheduleViewModel>()
    var moduleCode by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }
    val openDialogModule = remember { mutableStateOf(false)  }

    AlertDialog(
        title = {Text(text = "Add a Day to Your Schedule", modifier = Modifier,
            style = MaterialTheme.typography.headlineMedium)},
        text = {Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ){
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Module Code:", modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall)
                    Box(
                        modifier = Modifier
                            .clickable { openDialogModule.value = true }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Module",
                        )
                    }
                    if (openDialogModule.value) {
                        ModuleCreateDialog(openDialog = openDialogModule, openDialog2 = openDialog)
                    }
                }
                val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
                if (modules.isNotEmpty()) {
                    val moduleTitles = makeArrayOfModuleCodes(modules)

                    var expanded by remember { mutableStateOf(false) }
                    var selectedText by remember { mutableStateOf(moduleTitles[0]) }


                    Row {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }) {
                            TextField(
                                value = selectedText,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }) {
                                moduleTitles.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedText = item
                                            expanded = false
                                            //Toast.makeText(context, item, Toast.LENGTH_SHORT)
                                        })

                                }
                            }
                        }
                    }
                    moduleCode = selectedText
                }

                Spacer(modifier = Modifier.size(10.dp))

                Text("Start Time:", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time in HH:mm format") })

                Spacer(modifier = Modifier.size(10.dp))

                Text("End Time:", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
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
                        moduleCode = moduleCode,
                        status = 0
                    )
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
    val openDialogModule = remember { mutableStateOf(false)  }
    AlertDialog(
        title = {Text(text = "Edit Schedule Item ", modifier = Modifier,
            style = MaterialTheme.typography.headlineMedium)},
        text = {Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ){
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Module Code:", modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall)
                    Box(
                        modifier = Modifier
                            .clickable { openDialogModule.value = true }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Module",
                        )
                    }
                    if (openDialogModule.value) {
                        ModuleCreateDialog(openDialog = openDialogModule, openDialog2 = openDialog)
                    }
                }
                val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
                if (modules.isNotEmpty()) {
                    val moduleTitles = makeArrayOfModuleCodes(modules)

                    var expanded by remember { mutableStateOf(false) }
                    var selectedText by remember { mutableStateOf(moduleTitles[0]) }


                    Row {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }) {
                            TextField(
                                value = selectedText,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }) {
                                moduleTitles.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedText = item
                                            expanded = false
                                            //Toast.makeText(context, item, Toast.LENGTH_SHORT)
                                        })

                                }
                            }
                        }
                    }
                    moduleCode = selectedText
                }
                Spacer(modifier = Modifier.size(10.dp))

                Text("Start Time:", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time in HH:mm format") })

                Spacer(modifier = Modifier.size(10.dp))

                Text("End Time:", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
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
                            moduleCode = moduleCode,
                            status = 0
                        )
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
    val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
    if (modules.isNotEmpty()) {
        val module = findModule(modules, time.moduleCode)
        AlertDialog(
            title = { Text(text = "Lecture", modifier = Modifier,
                style = MaterialTheme.typography.headlineMedium) },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Module Title: \n ${module.moduleTitle} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Module Code: \n ${time.moduleCode} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Latitude: \n ${module.lat} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Longitude: \n ${module.long} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Time: \n ${time.startTime} - ${time.endTime}",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )

                }
            },
            onDismissRequest = { openDialog.value = false },
            dismissButton = {
                Button(onClick = { openDialog.value = false })
                {
                    Text(text = "Dismiss")
                }
            },
            confirmButton = {
                Button(onClick =
                {
                    openDialogEdit.value =
                        true //Bring the user to a part that allows them to edit the details
                    openDialog.value = false
                }
                )
                {
                    Text(text = "Edit")
                }
            }
        )
    }
}

//This is used to delete a timetable entry from the database, when sizyk merges with master
// update it so it only changes status
@Composable
fun DialogDelete( openDialog: MutableState<Boolean>, time: TimetableEntity){
    val viewModel = viewModel<ScheduleViewModel>()
    val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
    if (modules.isNotEmpty()) {
        val module = findModule(modules, time.moduleCode)
        AlertDialog(
            title = {Text(text = "Are you sure you want to delete the following lecture from your schedule?")},
            text = {Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ){
                    Text(
                        text = "Module Title: \n ${module.moduleTitle} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Module Code: \n ${time.moduleCode} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Latitude: \n ${module.lat} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Longitude: \n ${module.long} ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Time: \n ${time.startTime} - ${time.endTime}",
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

            }
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
                    openDialog.value = false},
                    colors = ButtonDefaults.buttonColors( containerColor = Color.Red)
                )
                {
                    Text(text = "Delete")
                }
                }
        )
    }
}


@Composable
fun CreateButtons(
    time: TimetableEntity) {
    val openDialogView = remember { mutableStateOf(false)  }
    val openDialogDelete = remember { mutableStateOf(false)  }
    val openDialogEdit = remember{ mutableStateOf(false)  }
    val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
    if (modules.isNotEmpty()) {
        val module = findModule(modules, time.moduleCode)

        if (openDialogView.value) {
            DialogView(openDialogView, openDialogEdit, time) //This brings up the view alert
        }
        if (openDialogDelete.value) {
            DialogDelete(openDialogDelete, time) //This brings up the delete alert
        }
        if (openDialogEdit.value) {
            DialogEdit(openDialog = openDialogEdit, time = time) //This brings up the edit alert
        }
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .height(140.dp)
                .padding(12.dp)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(120.dp)
            ) {

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        openDialogView.value = true
                    } //This is to let the user view the entire module data
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Column {
                        Text(
                            text = module.moduleTitle,
                            style = MaterialTheme.typography.headlineSmall
                        ) //This is going to be the moduleName
                        Text(text = "Module Code: ${time.moduleCode} ")
                        Text(text = "Latitude: ${module.lat} Longitude: ${module.long}")
                        Text(text = "Time: ${time.startTime} - ${time.endTime}")

                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .fillMaxHeight()
                        .clickable {
                            openDialogDelete.value = true
                        }// This will remove the module from the user's view
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", Modifier.fillMaxSize())
                }
            }
        }
    }

}

