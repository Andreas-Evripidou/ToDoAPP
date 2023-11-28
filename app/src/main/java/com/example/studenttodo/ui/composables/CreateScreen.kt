package com.example.studenttodo.ui.composables

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun SubmitButton(onClick: ()-> Unit){
    Button(
        onClick = {
            onClick()

        }){
        Text("Submit")

    }

}
@Composable
fun displayError(){
    Text("Please fill in all required fields (with *)", color = MaterialTheme.colorScheme.error)
}
@Composable
fun displayDateTimeError(){
    Text("Please enter a valid date and time in the correct format", color = MaterialTheme.colorScheme.error)
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen() {
    val viewModel = viewModel<CreateViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var taskname by remember { mutableStateOf("") }
        var taskdescription by remember { mutableStateOf("") }
        var locationradius by remember { mutableStateOf("") }

        Row {
            Text("Task Name")
            Text(
                " *",
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = taskname,
            onValueChange = { taskname = it },
            label = {

                Text("task title")
            })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("Task Description")
            Text(
                " *",
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = taskdescription,
            onValueChange = { taskdescription = it },
            label = { Text("task description") })

        Spacer(modifier = Modifier.height(16.dp))
        val choices = listOf("low", "medium", "high")
        val (priority, onSelected) = remember { mutableStateOf(choices[0]) }
        Row {
            Text("Task Priority")
            Text(
                " *",
                color = Color.Red
            )
        }
        choices.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (text == priority),
                    onClick = { onSelected(text) }
                )
                Text(text)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))


        var date by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("") }


        var day by remember { mutableStateOf("") }
        var month by remember { mutableStateOf("") }
        var year by remember { mutableStateOf("") }
        var showDateTimeFields by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDateTimeFields = !showDateTimeFields }
            ) {
                Checkbox(
                    checked = showDateTimeFields,
                    onCheckedChange = { showDateTimeFields = it })
                Text("Add reminder date & time")
            }

            if (showDateTimeFields) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier.width(70.dp),
                        value = day,
                        onValueChange = { textInput ->

                            day = textInput.take(2)

                        },
                        label = { Text("DD") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Text("/")

                    OutlinedTextField(
                        modifier = Modifier.width(70.dp),
                        value = month,
                        onValueChange = { textInput ->

                            month = textInput.take(2)

                        },
                        label = { Text("MM") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Text("/")


                    OutlinedTextField(
                        modifier = Modifier.width(80.dp),
                        value = year,
                        onValueChange = { textInput ->
                            year = textInput.take(4)

                        },
                        label = { Text("YYYY") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
                date = day.plus("/").plus(month).plus("/").plus(year)
                Spacer(modifier = Modifier.height(16.dp))

                var enteredHours by remember { mutableStateOf("") }
                var enteredMinutes by remember { mutableStateOf("") }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hours Text Field
                    OutlinedTextField(
                        modifier = Modifier.width(70.dp),
                        value = enteredHours,
                        onValueChange = { newInput ->

                            if (newInput.toIntOrNull() in 0..24) {
                                enteredHours = newInput.take(2)
                            }
                        },
                        label = { Text("HH") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Text(":")


                    OutlinedTextField(
                        modifier = Modifier.width(70.dp),
                        value = enteredMinutes,
                        onValueChange = { newInput ->

                            if (newInput.toIntOrNull() in 0..59) {
                                enteredMinutes = newInput.take(2)
                            }
                        },
                        label = { Text("MM") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                time = time.plus(enteredHours).plus(":").plus(enteredMinutes)

            }

        Text("Pick Location")
        var longitude by remember { mutableStateOf("") }
        var latitude by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier.width(120.dp),
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("longitude") })
        OutlinedTextField(
            modifier = Modifier.width(120.dp),
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("latitude") })
        Spacer(modifier = Modifier.height(16.dp))
        Text("Location Radius")
        OutlinedTextField(
            modifier = Modifier.width(120.dp),
            value = locationradius,
            onValueChange = { locationradius = it },
            label = { Text("radius") })
        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Image")

        var pickedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val context = LocalContext.current
        var selectedUri by remember { mutableStateOf("") }
        val imageFromGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri == null) {
                pickedImageBitmap = null
            } else {
                var selectedUri: Uri = uri
                val contentResolver: ContentResolver = context.contentResolver
                pickedImageBitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(contentResolver, uri)
                ).asImageBitmap()
            }
        }



        Column {
            pickedImageBitmap?.let { imageBitmap ->
                Image(imageBitmap, null)
            }
        }
        OutlinedButton(onClick = {
            imageFromGalleryLauncher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        ) {
            Text("Open Gallery")
        }

        Spacer(modifier = Modifier.height(16.dp))


        var showError by remember { mutableStateOf(false) }
        var showDateError by remember { mutableStateOf(false) }
        SubmitButton {
            var lDate = LocalDate.now()
            var lTime = LocalTime.now()


            if (taskname.isNotEmpty() && taskdescription.isNotEmpty()) {

                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                try {
                    lDate = LocalDate.parse(date, dateFormatter)
                    lTime = LocalTime.parse(time, timeFormatter)

                } catch (e: DateTimeParseException) {
                    // Error handling: Print an error message or take appropriate action

                }


                val todo = ToDoEntity(
                    title = taskname,
                    reminderDate = lDate,
                    reminderTime = lTime,

                    priority = choices.indexOf(priority) + 1,
                    status = 0,
                    description = taskdescription,
                    picture = selectedUri,
                    latitude = latitude,
                    longitude = longitude,
                    range = locationradius,
                    createdLatitude = "temp",
                    createdLongitude = "temp",
                    moduleCode = "temp"
                )
                viewModel.createToDo(todo)

            } else {
                showError = true
            }


        }
        if (showError) {
            displayError()
        }
        if (showDateError) {
            displayDateTimeError()
        }
    }

}




