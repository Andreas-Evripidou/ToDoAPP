package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.launch


//@Composable
//fun Greeding (name: String, modifier: Modifier = Modifier) {
//        Text(
//            text = "Hello $name!",
//            modifier = modifier
//        )
//    }

@Composable
fun HomeScreen() {
    /* TODO displays a list of tasks
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
                    reminderDate = "11/11/2023",
                    reminderTime = "12:00",
                    priority = "High",
                    latitude = "53.378862598206425",
                    longitude = "-1.479367317915664",
                    range = "10",
                    status = "New"
                )
            )
        }
    }
    Column(modifier = Modifier.fillMaxWidth())
    {
        Text(text = "Test")
        (1..99).forEach {// once connected to database todo 1..datbase size
            dispTasks()//(databseItem[])
        }
        Button(onClick = ::insertOnClick) { Text(text = "Add Data") }
    }
}
@Composable
fun dispTasks () {
    Column {
        Card(
            shape = RoundedCornerShape(size = 10.dp), modifier = Modifier.padding(5.dp)
        ){
            Text(text = "Hello Max", fontSize = 25.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(10.dp))
        }
    }

}
