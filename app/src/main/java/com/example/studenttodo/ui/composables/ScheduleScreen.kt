package com.example.studenttodo.ui.composables

import android.widget.Toast
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
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.ui.composables.components.ModuleCreateDialog
import com.example.studenttodo.ui.composables.components.SelectLocation
import com.example.studenttodo.ui.composables.components.SelectOrCreateModule
import com.example.studenttodo.ui.composables.components.SelectTime
import com.example.studenttodo.ui.composables.components.makeArrayOfModuleCodes
import com.example.studenttodo.viewmodels.CreateViewModel
import com.example.studenttodo.viewmodels.ScheduleViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun ScheduleScreen (modifier: Modifier = Modifier) {
    val times by viewModel<ScheduleViewModel>().timetable.collectAsState(initial = emptyList())
    val weekdays: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    Column (horizontalAlignment = Alignment.CenterHorizontally) {
    //This is the day of the week, repeat for each work day of the week
    weekdays.forEach { weekday ->
        Text(
            text = weekday,
            modifier = modifier,
            style = MaterialTheme.typography.headlineMedium
        )
        //This repeats for each data value matching the current day of the week
        val current = times.filter {it.day == weekday}
        current.forEach { time ->
            if (time.status ==0){ CreateButtons(time=time)}}

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val openDialog = remember { mutableStateOf(false)  }
            if (openDialog.value) {
                DialogAdd( openDialog, weekday)
            }
            Box(modifier = Modifier
                .clickable { openDialog.value = true }
            ) {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = "Add"
                )
            }
        }
        if (weekday != "Friday"){
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        }


        }
    }
}

//Make the module code a drop down and link the Module Title
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAdd(openDialog: MutableState<Boolean>, weekday: String){
    val viewModel = viewModel<ScheduleViewModel>()
    var moduleCode by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }
    val openCreateModuleDialog = remember { mutableStateOf(false)  }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var locationradius by remember { mutableStateOf("") }
    var itemType by remember { mutableStateOf("") }

    fun updateSelectedItemType(type: String){
        itemType = type
    }

    fun updatedSelectedLocation(lon: String, lat: String, radius: String){
        longitude = lon
        latitude = lat
        locationradius = radius
    }

    fun updateSelectedCreateModuleDialog (open: Boolean){
        openCreateModuleDialog.value = open
    }
    fun updateSelectedModuleCode(mc: String) {
        moduleCode = mc
    }

    AlertDialog(
        title = {Text(text = "Add an Item")},
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ){
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SelectOrCreateModule(::updateSelectedCreateModuleDialog,
                        ::updateSelectedModuleCode)
                    if (openCreateModuleDialog.value){
                        ModuleCreateDialog(
                            openDialog = openCreateModuleDialog,
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    ItemTypeDropDown(currentType = "Lecture", updateSelectedType = ::updateSelectedItemType)

                    Spacer(modifier = Modifier.size(10.dp))

                    Text("Start Time:", style = MaterialTheme.typography.headlineSmall)
                    fun updateSelectedStartTime(h: String, m: String){
                        startTime = h.plus(":").plus(m)
                    }
                    SelectTime(time =  startTime, updatedSelectedTime = ::updateSelectedStartTime )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text("End Time:",style = MaterialTheme.typography.headlineSmall)
                    fun updateSelectedEndTime(h: String, m: String){
                        endTime = h.plus(":").plus(m)
                    }
                    SelectTime(time =  endTime, updatedSelectedTime = ::updateSelectedEndTime )

                    Spacer(modifier = Modifier.size(10.dp))
                    SelectLocation(lon = longitude, lat = latitude, radius = locationradius, updateSelectedLoc = ::updatedSelectedLocation)
                }

            }
        },
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
                        long = longitude,
                        lat = latitude,
                        radius = locationradius,
                        itemType = itemType,
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
    var itemType by remember { mutableStateOf(time.itemType) }

    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var locationradius by remember { mutableStateOf("") }

    fun updateSelectedItemType(type: String){
        itemType = type
    }
    fun updatedSelectedLocation(lon: String, lat: String, radius: String){
        longitude = lon
        latitude = lat
        locationradius = radius
    }

    fun updateSelectedModuleCode(mc: String) {
        moduleCode = mc
    }
    fun updateSelectedCreateModuleDialog (open: Boolean){
        openDialogModule.value = open
    }
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
                        ModuleCreateDialog(openDialog = openDialogModule)
                    }
                }
                SelectOrCreateModule(openDialog = ::updateSelectedCreateModuleDialog,
                    updateSelectedModuleCode = ::updateSelectedModuleCode)
                Spacer(modifier = Modifier.size(10.dp))

                ItemTypeDropDown(currentType = time.itemType, updateSelectedType = ::updateSelectedItemType)

                Spacer(modifier = Modifier.size(10.dp))

                Text("Start Time:", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                fun updateSelectedStartTime(h: String, m: String){
                    startTime = h.plus(":").plus(m)
                }
                SelectTime(time =  startTime, updatedSelectedTime = ::updateSelectedStartTime )

                Spacer(modifier = Modifier.size(10.dp))

                Text("End Time:", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                fun updateSelectedEndTime(h: String, m: String){
                    endTime = h.plus(":").plus(m)
                }
                SelectTime(time =  endTime, updatedSelectedTime = ::updateSelectedEndTime )
                Spacer(modifier = Modifier.size(10.dp))
                SelectLocation(lon = longitude, lat = latitude, radius = locationradius, updateSelectedLoc = ::updatedSelectedLocation)
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
                            itemType = itemType,
                            long = longitude,
                            lat = latitude,
                            radius = locationradius,
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


fun findModule(modules: List<ModuleEntity>, currentModuleCode: String): ModuleEntity {
    var requiredModule = modules[0]
    for (module in modules) {
        if (module.moduleCode == currentModuleCode) {
            requiredModule = module
        }
    }

    return requiredModule
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTypeDropDown(currentType: String, updateSelectedType: (s: String) -> Unit){
    val choices = listOf("Lecture", "Lab", "Seminar")
    var selectedType by remember { mutableStateOf(currentType) }
    Row {
        Text("Timetable Item Type:",
            style = MaterialTheme.typography.headlineSmall)
        Text(" *", color = Color.Red)
    }
    var expanded by remember { mutableStateOf(false) }
    Row (
        verticalAlignment = Alignment.CenterVertically)
    {
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(2f),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedType,
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
                            selectedType = item
                            updateSelectedType(item)
                            expanded = false
                        })
                }
            }
        }
    }
}

