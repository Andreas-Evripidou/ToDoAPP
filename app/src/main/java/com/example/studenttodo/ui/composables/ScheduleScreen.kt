package com.example.studenttodo.ui.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.studenttodo.data.ToDoDatabase
import kotlinx.coroutines.launch
import com.example.studenttodo.entities.ModuleEntity

@Composable
fun ScheduleScreen (name: String, modifier: Modifier = Modifier) {

    val buttonScope = rememberCoroutineScope()
    val dao = ToDoDatabase.getDB(LocalContext.current).moduleDAO()
    fun insertOnClick() {
        buttonScope.launch {
            dao.insert(
                ModuleEntity(
                    moduleCode = "COM3117",
                    lat = "53.378862598206430",
                    long = "1.279367317915664",
                    moduleTitle = "Text Processing"
                )
            )

        }
    }
    Button(onClick = ::insertOnClick) { Text(text = "Add Data") }
}