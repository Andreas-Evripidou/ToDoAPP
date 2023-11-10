package com.example.studenttodo.ui.composables

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.ArchiveViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

@Composable
fun ArchiveScreenTemp() {
    Column {

        var bold by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Bold")
            Spacer(modifier = Modifier.size(8.dp))
            Switch(checked = bold, onCheckedChange = { bold = it })
        }
        Spacer(modifier = Modifier.size(38.dp))
        val choices = listOf("small", "medium", "large", "extra large")
        val (size, onSelected) = remember { mutableStateOf(choices[1]) }
        Text(text = "Size")
        choices.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = (text == size), onClick = { onSelected(text) })
                Text(text)
            }
        }
        Spacer(modifier = Modifier.size(38.dp))

        val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        var selectedDays by remember { mutableStateOf(BooleanArray(weekdays.size) { false }) }
        weekdays.forEachIndexed { index, weekday ->
            var thisDay by remember { mutableStateOf(selectedDays[index]) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(weekday)
                Checkbox(
                    checked = thisDay,
                    onCheckedChange = {
                        thisDay = !thisDay
                        selectedDays[index] = thisDay
                    })
            }

        }
        Button({ Log.i("LogLog", selectedDays.contentToString()) }) {
            Text(text = "Log")
        }

    }
}

@Composable
fun ShowToDo(
    todo: ToDoEntity,
    onDelete: (ToDoEntity) -> Unit,
    onRestore: (ToDoEntity) -> Unit
){
    Card (modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(12.dp)
    )
    {
        Row (modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
        ) {

            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { /* TODO */ }
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Column(
                ) {
                    Text(text = todo.title, style = MaterialTheme.typography.headlineMedium)
                    Text(text = "module placeholder", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Box(modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .clickable { onRestore(todo) }
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Restore", Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.size(16.dp))

            Box(modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .clickable { onDelete(todo) }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", Modifier.fillMaxSize())
            }
        }
    }
}


@Composable
fun ArchiveScreen(){
    val toDoScope = rememberCoroutineScope()
    val archiveViewModel = viewModel<ArchiveViewModel>()
    val todos by archiveViewModel.todos.collectAsState(initial = emptyList())
    Column  (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start)
    {
        todos.forEach { todo ->
            ShowToDo(
                todo = todo,
                onDelete = {archiveViewModel.deleteToDo(it)},
                onRestore = {archiveViewModel.restoreToDo(it)}
            )
        }
    }
}


