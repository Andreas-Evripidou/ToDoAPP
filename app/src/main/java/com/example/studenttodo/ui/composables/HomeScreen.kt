package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
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
                    moduleCode = "COM3117",
                )
            )
        }
    }
    Row(modifier = Modifier.fillMaxWidth())
    {
        Text(text = "Test")
        Button(onClick = ::insertOnClick) { Text(text = "Add Data") }
    }
}

