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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


//@Composable
//fun Greeding (name: String, modifier: Modifier = Modifier) {
//        Text(
//            text = "Hello $name!",
//            modifier = modifier
//        )
//    }

@Composable
fun HomeScreen() {
    /*
    * TODO Colour based on priority ability to mark as completed
    * TODO Click to open page to update/view
    * TODO ordered by date/priority
    */
    val buttonScope = rememberCoroutineScope()
    val dao = ToDoDatabase.getDB(LocalContext.current).todoDao()
    fun insertOnClick() {
        buttonScope.launch {
            dao.insert(
                ToDoEntity(
                    title= "Do Text Assignment",
                    reminderDate = LocalDate.parse("11/11/2023", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    reminderTime = LocalTime.parse("13:10", DateTimeFormatter.ofPattern("HH:mm")),
                    priority = 2,
                    latitude = "53.378862598206425",
                    longitude = "-1.479367317915664",
                    range = "10",
                    status = 0,
                    description = "This is the description for to do my text assignment",
                    picture = "Resources/1.jpg",
                    createdLatitude = "53.378862598206425",
                    createdLongitude = "-1.279367317915664",
                    moduleCode = "COM3118",
                )
            )
        }
    }

    val toDoScope = rememberCoroutineScope()
    val homeViewModel = viewModel<HomeViewModel>()
    val todos by homeViewModel.todos.collectAsState(initial = emptyList())
    Column  (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start)
    {
        todos.forEach { todo ->
            dispTasks(
                todo = todo,
                onArchive = {homeViewModel.archiveToDo(it)},
                onViewMore = {homeViewModel.viewMore(it)}
            )
        }

        Text(text = "Test")
        Button(onClick = ::insertOnClick) { Text(text = "Add Data") }
    }
}
@Composable
fun dispTasks (
    todo: ToDoEntity,
    onArchive: (ToDoEntity) -> Unit,
    onViewMore: (ToDoEntity) -> Unit
    ){
        Card (modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .height(110.dp)
            .padding(12.dp)
        )
        {
            Row (modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
                        Text(text = "module placeholder")
                        Text(text = "Due Date: ${todo.reminderDate}")
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                Box(modifier = Modifier
                    .width(50.dp)
                    .fillMaxHeight()
                    .clickable { onViewMore(todo) }
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "See More", Modifier.fillMaxSize())
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
