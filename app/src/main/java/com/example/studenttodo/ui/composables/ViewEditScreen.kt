package com.example.studenttodo.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ViewEditScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        var taskname by remember { mutableStateOf("") }
        var taskdescription by remember { mutableStateOf("") }
        var locationradius by remember { mutableStateOf("") }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        var taskDateTime = LocalDateTime.now().format(formatter)
        var distanceRadiusPlaceholder = "1.5km"
        Row(Modifier.fillMaxWidth().padding(top = 50.dp),
            horizontalArrangement  =  Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top) {

            Text(
                text = "Date/Time: "
            )
            Text( modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                text = taskDateTime
            )
        }

        Row(Modifier.fillMaxWidth().padding(top = 50.dp),
            horizontalArrangement  =  Arrangement.SpaceEvenly) {

            Text(
                text = "Location: "
            )

            Text(
                text = "Location Placeholder",
                modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                fontSize = 24.sp
            )
        }

        Row(Modifier.fillMaxWidth().padding(top = 50.dp),
            horizontalArrangement  =  Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top) {

            Text(
                text = "Distance Radius: "
            )

            Text(
                modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                text = distanceRadiusPlaceholder,
                fontSize = 16.sp
            )
        }

        Row(Modifier.fillMaxWidth().padding(top = 50.dp, bottom = 50.dp),
            horizontalArrangement  =  Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top) {

            Text(
                text = "Picture: "
            )

            Text(
                text = "Picture Placeholder",
                modifier = Modifier.border(BorderStroke(1.dp, Color.Black)),
                fontSize = 24.sp
            )
            }

        SubmitButton(){
        }

        }
        @Composable
        fun SubmitButton(onClick: ()-> Unit) {
            Button(onClick = { onClick() }) {
                Text("Submit")
            }
        }


    }