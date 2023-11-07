package com.example.studenttodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.ui.theme.StudentToDoTheme
import kotlinx.coroutines.launch
import com.example.studenttodo.data.*
import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        SaveButton()
    }


}

@Composable
fun SaveButton() {
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
    Row(modifier = Modifier.fillMaxWidth())
    {
        Text(text = "Test")
        Button(onClick = ::insertOnClick) { Text(text = "Add Data") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudentToDoTheme {
        Greeting("Android")
    }
}