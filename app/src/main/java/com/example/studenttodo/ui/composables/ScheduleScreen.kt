package com.example.studenttodo.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScheduleScreen (name: String, modifier: Modifier = Modifier) {
    Text(
    text = "Hello $name!",
    modifier = modifier
    )
}