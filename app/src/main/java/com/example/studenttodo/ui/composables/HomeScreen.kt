package com.example.studenttodo.ui.composables

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.HomeViewModel


@Composable
fun HomeScreen() {
    /*
    *
    * TODO Click to open page to update/view
    *
    */
    val homeViewModel = viewModel<HomeViewModel>()
    val todos by homeViewModel.todos.collectAsState(initial = emptyList())
    Column  (modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    )
    {
        todos.forEach { todo ->
            dispTasks(
                todo = todo,
                borderColor = homeViewModel.getBorderColor(todo),
                onArchive = {homeViewModel.archiveToDo(it)},
                onViewMore = {homeViewModel.viewMore(it)}
            )
        }

    }
}
@Composable
fun dispTasks (
    todo: ToDoEntity,
    onArchive: (ToDoEntity) -> Unit,
    onViewMore: (ToDoEntity) -> Unit,
    borderColor: Int,
    ){
        Card (modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 10.dp),
            border = BorderStroke(1.dp, Color(borderColor))
        ) {

            Row (modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
            ) {

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onViewMore(todo) }
                ) {
                    Column (modifier = Modifier.padding(start = 6.dp) ){
                        Text(text = todo.title, style = MaterialTheme.typography.headlineSmall)
                        Text(text = "Module: ${todo.moduleCode}", style = MaterialTheme.typography.labelSmall)
                        Text(text = "Due Date: ${todo.reminderDateFormatted}", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                Box(modifier = Modifier
                    .width(50.dp)
                    .fillMaxHeight()
                    .clickable { onArchive(todo) }
                ) {
                    Icon(Icons.Filled.Done, contentDescription = "Done", Modifier.fillMaxSize())
                }
            }
        }
}
