package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.ui.composables.DisplayDateTimeError2
import com.example.studenttodo.ui.composables.DisplayError1
import com.example.studenttodo.viewmodels.ScheduleViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ScheduleItemAddOrUpdate(openDialog: MutableState<Boolean>, scheduleItem: TimetableEntity){
    val scheduleViewModel = viewModel<ScheduleViewModel>()
    var moduleCode by remember { mutableStateOf(scheduleItem.moduleCode) }
    var startTime by remember { mutableStateOf(scheduleItem.startTime.toString()) }
    var endTime by remember { mutableStateOf(scheduleItem.endTime.toString()) }

    var itemType by remember { mutableStateOf(scheduleItem.itemType) }

    var latitude by remember { mutableStateOf(scheduleItem.lat) }
    var longitude by remember { mutableStateOf(scheduleItem.long) }
    var locationRadius by remember { mutableStateOf(scheduleItem.radius) }

    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }

    fun updateSelectedItemType(type: String){
        itemType = type
    }

    fun updatedSelectedLocation(lon: String, lat: String, radius: String){
        longitude = lon
        latitude = lat
        locationRadius = radius
    }

    fun updateSelectedModuleCode(mc: String) {
        moduleCode = mc
    }

    fun updateSelectedStartTime(h: String, m: String)
    { startTime = h.plus(":").plus(m) }

    fun updateSelectedEndTime(h: String, m: String)
    { endTime = h.plus(":").plus(m) }

    AlertDialog(
        title = { Text(text = "Add an Item") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ){
                Column(verticalArrangement = Arrangement.spacedBy(4.dp))
                {
                    if (showError) {
                        DisplayError1()
                    }
                    if(showDateError){
                        DisplayDateTimeError2()
                    }

                    SelectOrCreateModule(::updateSelectedModuleCode, moduleCode = scheduleItem.moduleCode)

                    Spacer(modifier = Modifier.size(10.dp))

                    ItemTypeDropDown(currentType = itemType, updateSelectedType = ::updateSelectedItemType)

                    Spacer(modifier = Modifier.size(10.dp))

                    Text("Start Time:", style = MaterialTheme.typography.headlineSmall)


                    SelectTime(time =  startTime, updatedSelectedTime = ::updateSelectedStartTime )

                    Spacer(modifier = Modifier.size(10.dp))

                    Text("End Time:",style = MaterialTheme.typography.headlineSmall)

                    SelectTime(time =  endTime, updatedSelectedTime = ::updateSelectedEndTime )

                    Spacer(modifier = Modifier.size(10.dp))
                    SelectLocation(lon = longitude, lat = latitude, radius = locationRadius, updateSelectedLoc = ::updatedSelectedLocation)
                }

            }
        },
        onDismissRequest = {openDialog.value = false},
        dismissButton =
        { Button(onClick = { openDialog.value = false})
            { Text(text = "Dismiss") } },
        confirmButton = {
            Button(onClick =
            {if (scheduleItem.day.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && moduleCode.isNotEmpty()) {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                try {
                    val newStartTime = LocalTime.parse(startTime, timeFormatter)
                    val newEndTime = LocalTime.parse(endTime, timeFormatter)
                    val entry = TimetableEntity(
                        id = scheduleItem.id,
                        day = scheduleItem.day,
                        startTime = newStartTime,
                        endTime = newEndTime,
                        moduleCode = moduleCode,
                        long = longitude,
                        lat = latitude,
                        radius = locationRadius,
                        itemType = itemType,
                        status = scheduleItem.status
                    )
                    scheduleViewModel.updateOrInsert(entry)
                    openDialog.value = false

                }
                catch(e: DateTimeParseException){
                    showDateError = true
                }

            }
            else { showError = true } })
            {
                Text(text = "Submit")
            }
        }
    )
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

@Composable
fun DeleteScheduleDialog( item: TimetableEntity, openDialog: MutableState<Boolean>){
    val scheduleViewModel = viewModel<ScheduleViewModel>()

    AlertDialog(
        title = {Text(text = "Delete Schedule Item")},
        text = { Text(text = "Are you sure you want to permanent delete this Item? This action can not be restored")},
        onDismissRequest = {openDialog.value = false},
        dismissButton = {
            Button(onClick = { openDialog.value = false})
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick = {
                openDialog.value = false
                scheduleViewModel.deleteTimetable(item) },
                colors = ButtonDefaults.buttonColors( containerColor = Color.Red)
            )
            {
                Text(text = "Delete")
            }
        }
    )
}