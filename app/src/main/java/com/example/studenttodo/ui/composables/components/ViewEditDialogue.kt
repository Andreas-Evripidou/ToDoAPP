package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions.Dismiss
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studenttodo.ui.composables.SubmitButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CustomDialogue(onDismissRequest: () -> Unit)
    {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                //Title
                Row(
                    Modifier.fillMaxWidth().padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ){
                    Text(
                        text = "Title: "
                    )
                    Text(
                        text = "Task..."
                    )

                }

                //Description
                Row(
                    Modifier.fillMaxWidth().padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ){
                    Text(
                        text = "Description: "
                    )
                    Text(
                        text = "Description",
                        fontSize = 24.sp
                    )
                }

                //Date&Time
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                var taskDateTime = LocalDateTime.now().format(formatter)
                var distanceRadiusPlaceholder = "1.5km"
                Row(
                    Modifier.fillMaxWidth().padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {

                    Text(
                        text = "Date/Time: "
                    )
                    Text(
                        text = taskDateTime
                    )
                }

                //Location
                Row(
                    Modifier.fillMaxWidth().padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(
                        text = "Location: "
                    )

                    Text(
                        text = "Location Placeholder",
                        fontSize = 24.sp
                    )
                }

                //Distance Radius
                Row(
                    Modifier.fillMaxWidth().padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {

                    Text(
                        text = "Distance Radius: "
                    )

                    Text(
                        text = distanceRadiusPlaceholder,
                        fontSize = 16.sp
                    )
                }

                //Picture
                Row(
                    Modifier.fillMaxWidth().padding(top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {


                    Text(
                        text = "Picture: "
                    )

                    Text(
                        text = "Picture Placeholder",
                        fontSize = 24.sp
                    )
                }
                Button(
                    onClick = {onDismissRequest()}
                ){
                }
            }
        }
    }
}
