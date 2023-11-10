package com.example.studenttodo.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleScreen (name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.Start

    ) {
        //This is the day of the week, repeat for each work day of the week
        Text(
            text = "Day Of The Week",
            modifier = modifier
        )
        // This the elevated button that shows the lesson. This repeats for every lesson in that day
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            ElevatedButton( onClick = {/*TODO*/}){
                Text(text= " Module Name: $name!                " +
                        " \n Module Code:                       " +
                        " \n Time:                              " +
                        "\n Location:                           ")}

        }
        Spacer(modifier = Modifier.height(5.dp) )

        //This is the icon part underneath the button weekly lesson button. Only show one per day
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
           IconButton(onClick = {/*TODO*/}){ Icon(Icons.Filled.AddCircle, contentDescription = "Add")}
        }
    }
}