package com.example.studenttodo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studenttodo.ui.theme.StudentToDoTheme

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
                    NavigationScaffold()
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Buttons(){
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

data class NavigationComponent(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val screenID: Int /*TODO enum */
){
    @Composable
    fun iconStyled(selectedScreenID: Int){
        if (selectedScreenID == screenID)
            Icon(filledIcon, contentDescription = title)
        else
            Icon(outlinedIcon, contentDescription = title)
    }
}

@Composable
fun TextIconButton(content: String, icon: ImageVector){
    IconButton(onClick = { /*TODO*/ }) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = content)
            Text(content, fontSize = 8.sp)
        }
    }
}

@Composable
fun IconButtonsPage(){
    Row {
        TextIconButton(content = "Home", icon = Icons.Filled.Home)
        TextIconButton(content = "Set", icon = Icons.Filled.Settings)
        TextIconButton(content = "Notify", icon = Icons.Filled.Notifications)
    }
}

val navigationList = listOf(
    NavigationComponent("Home", Icons.Filled.Home, Icons.Outlined.Home, ScreenID.HOME),
    NavigationComponent("Test Content", Icons.Filled.Star, Icons.Outlined.Star, ScreenID.TEST),
    NavigationComponent("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, ScreenID.SETTING)
)

@Composable
fun Selectors(){
    Column {

        var bold by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Bold")
            Spacer(modifier = Modifier.size(8.dp))
            Switch(checked = bold, onCheckedChange = { bold = it })
        }
        Spacer(modifier = Modifier.size(38.dp))
        val choices = listOf("small", "medium", "large", "extra large")
        val (size, onSelected) = remember { mutableStateOf(choices[1]) }
        Text(text = "Size")
        choices.forEach { text ->
            Row (verticalAlignment = Alignment.CenterVertically){
                RadioButton(selected = (text == size), onClick = { onSelected(text) })
                Text(text)
            }
        }
        Spacer(modifier = Modifier.size(38.dp))

        val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        var selectedDays by remember { mutableStateOf(BooleanArray(weekdays.size) {false}) }
        weekdays.forEachIndexed { index,weekday ->
            var thisDay by remember{ mutableStateOf(selectedDays[index]) }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(weekday)
                Checkbox(
                    checked = thisDay,
                    onCheckedChange = {
                        thisDay = !thisDay
                        selectedDays[index] = thisDay
                    } )
            }

        }
        Button({ Log.i("LogLog", selectedDays.contentToString())}){
            Text(text = "Log")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(){
    var text by remember { mutableStateOf ("") }
    Text(text = "My Name")
    TextField(value = text, onValueChange = {text = it})
}


@Composable
fun NavigationComponents(
    selectedScreenID: Int,
    updateSelected: (screenID: Int, newTitle: String) -> Unit
){
    NavigationBar {
        navigationList.forEach { navigation ->
            NavigationBarItem(
                label = { Text(navigation.title) },
                icon = { navigation.iconStyled(selectedScreenID)},
                selected = (selectedScreenID == navigation.screenID),
                onClick = { updateSelected(navigation.screenID, navigation.title) }
            )
        }
    }
}

@Composable
fun ScreenComponents(selectedScreenID: Int){
    when (selectedScreenID) {
        ScreenID.HOME -> Screen(name = "Andreas")
        ScreenID.TEST -> Buttons()
        ScreenID.SETTING -> Selectors()
    }

}


object ScreenID {
    const val HOME = 0
    const val TEST = 1
    const val SETTING = 2

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(screen: Int) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                when (screen) {
                    ScreenID.HOME -> "Home"
                    ScreenID.TEST -> "Test content"
                    ScreenID.SETTING -> "Settings"
                    else -> "Title Missing"
                }
            )
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScaffold(){
    var selectedScreenID by remember { mutableStateOf( ScreenID.HOME) }
    var title by remember {
        mutableStateOf("HOME") //TODO tidy this
    }
    fun updatedSelectedID(id: Int, newTitle: String){
        //Note: this is a convenience function
        selectedScreenID = id
        title = newTitle
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
        bottomBar = {
            NavigationComponents(
                selectedScreenID,
                updateSelected = ::updatedSelectedID)
            // Note: reference to function using ::
        },
        content = {
            Column ( modifier = Modifier
                .padding(it)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start)
            {
                ScreenComponents(selectedScreenID = selectedScreenID)
            }
        })
}

@Composable
fun  Screen(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudentToDoTheme {
        NavigationScaffold()
    }
}