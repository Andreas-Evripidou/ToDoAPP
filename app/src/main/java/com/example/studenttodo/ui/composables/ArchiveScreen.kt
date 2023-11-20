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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.ArchiveViewModel

@Composable
fun ShowToDo(
    todo: ToDoEntity,
    onDelete: (ToDoEntity) -> Unit,
    onRestore: (ToDoEntity) -> Unit
){
    val openDialog = remember { mutableStateOf(false)  }
    if (openDialog.value) {
        DialogDelete(onDelete = onDelete, todo, openDialog)
    }
    Card (modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(bottom = 10.dp)
    )
    {
        Row (modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Spacer(modifier = Modifier.size(4.dp))
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { /* TODO add the ToDo information dialog*/ }
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Column(
                ) {
                    Text(text = todo.title, style = MaterialTheme.typography.headlineMedium)
                    Text(text = "Module: ${todo.moduleCode}", style = MaterialTheme.typography.labelSmall)
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
                .clickable { openDialog.value = true }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", Modifier.fillMaxSize())
            }

        }
    }
}

@Composable
fun DialogDelete(onDelete: (ToDoEntity) -> Unit, todo: ToDoEntity, openDialog: MutableState<Boolean>){
    AlertDialog(
        title = {Text(text = "Delete ToDo")},
        text = { Text(text = "Are you sure you want to permanent delete this ToDo? This action can not be restored")},
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
                onDelete(todo) })
            {
                Text(text = "Delete")
            }
        }
    ) 
}

@Composable
fun ArchiveScreen(){
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


