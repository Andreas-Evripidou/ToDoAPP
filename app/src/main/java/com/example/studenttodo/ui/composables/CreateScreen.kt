package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CreateScreen(){
    Column (
//        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(text = "Primary Actions")
        Button(onClick = { }) { Text(text = "Filled") }
        FilledTonalButton(onClick = { /*TODO*/ }) { Text(text = "Tonal") }
        ElevatedButton(onClick = { /*TODO*/ }) { Text(text = "Elevated") }
        Text(text = "Medium emphasis")
        OutlinedButton(onClick = { /*TODO*/ }) { Text(text = "Outlined") }
        Text(text = "Low emphasis")
        TextButton(onClick = { /*TODO*/ }) { Text(text = "Text Button") }
    }


}