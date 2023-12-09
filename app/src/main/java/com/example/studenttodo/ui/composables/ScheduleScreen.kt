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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.ui.composables.components.DeleteScheduleDialog
import com.example.studenttodo.ui.composables.components.ScheduleItemAddOrUpdate
import com.example.studenttodo.viewmodels.CreateViewModel
import com.example.studenttodo.viewmodels.ScheduleViewModel


@Composable
fun ScheduleScreen (modifier: Modifier = Modifier) {
    val scheduleViewModel = viewModel<ScheduleViewModel>()
    val scheduleItems by viewModel<ScheduleViewModel>().timetable.collectAsState(initial = emptyList())
    val weekdays: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")


    Column (horizontalAlignment = Alignment.CenterHorizontally)
    {
        //This is the day of the week, repeat for each work day of the week
        weekdays.forEach { weekday ->
            val openAddDialog = remember { mutableStateOf(false)  }

            if (openAddDialog.value) {
                ScheduleItemAddOrUpdate( openAddDialog, scheduleViewModel.emptyTimetableItem(weekday))
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = weekday,
                    modifier = modifier,
                    style = MaterialTheme.typography.headlineMedium)

                IconButton(onClick = { openAddDialog.value = true })
                { Icon(Icons.Filled.AddCircle, contentDescription = "Add") }
            }

            //This repeats for each data value matching the current day of the week
            val current = scheduleItems.filter {it.day == weekday}
            current.forEach { scheduleItem ->
                if (scheduleItem.status ==0){
                    ScheduleItem(scheduleItem = scheduleItem)
                    Spacer(modifier = Modifier.size(4.dp))
                }
        }

        if (weekday != "Friday"){
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        }
        Spacer(modifier = Modifier.size(4.dp))

        }
    }
}

@Composable
fun DisplayError1(){
    Text("Please fill in all fields", color = MaterialTheme.colorScheme.error)
}
@Composable
fun DisplayDateTimeError2(){
    Text("Please enter valid times", color = MaterialTheme.colorScheme.error)
}

@Composable
fun ScheduleItem(scheduleItem: TimetableEntity) {
    val openDialogViewEdit = remember { mutableStateOf(false)  }
    val openDialogDelete = remember { mutableStateOf(false)  }
    val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
    if (modules.isNotEmpty()) {

        if (openDialogViewEdit.value) {
            ScheduleItemAddOrUpdate(openDialogViewEdit, scheduleItem) //This brings up the view alert
        }
        if (openDialogDelete.value) {
            DeleteScheduleDialog(scheduleItem, openDialogDelete) //This brings up the delete alert
        }

        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .height(100.dp))
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { openDialogViewEdit.value = true } )//This is to let the user view the entire module data
                {
                    Column (modifier = Modifier.padding(10.dp)){
                        Text(text = scheduleItem.itemType,
                            style = MaterialTheme.typography.headlineSmall)
                        Text(text = "Module Code: ${scheduleItem.moduleCode} ")
                        Text(text = "Time: ${scheduleItem.startTime} - ${scheduleItem.endTime}") }
                }

                Spacer(modifier = Modifier.size(8.dp))

                IconButton(
                    onClick = { openDialogDelete.value = true },
                    modifier = Modifier
                        .width(40.dp)
                        .fillMaxHeight()
                        .padding(10.dp))
                { Icon(Icons.Filled.Delete, contentDescription = "Delete", Modifier.fillMaxSize()) }
            }
        }
    }

}

