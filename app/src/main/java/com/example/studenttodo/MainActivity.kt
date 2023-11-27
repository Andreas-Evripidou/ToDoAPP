package com.example.studenttodo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.ui.composables.NavigationScaffold
import com.example.studenttodo.ui.theme.StudentToDoTheme
import com.example.studenttodo.viewmodels.ArchiveViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val archiveViewModel = viewModel<ArchiveViewModel>()
            val toDoDAO = ToDoDatabase.getDB(LocalContext.current).todoDao()
            val serviceIntent = Intent(this, NotifsBackground::class.java)
            startService(serviceIntent)
            StudentToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationScaffold()
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    StudentToDoTheme {
        NavigationScaffold()
    }
}