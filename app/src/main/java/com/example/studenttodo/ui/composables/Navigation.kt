package com.example.studenttodo.ui.composables

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

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

val navigationList = listOf(
    NavigationComponent("Home", Icons.Filled.Home, Icons.Outlined.Home, ScreenID.HOME),
    NavigationComponent("Create", Icons.Filled.AddCircle, Icons.Outlined.AddCircle, ScreenID.CREATE ),
    NavigationComponent("Archive", Icons.Filled.Star, Icons.Outlined.Star, ScreenID.ARCHIVE),
    NavigationComponent("Schedule", Icons.Filled.DateRange, Icons.Outlined.DateRange, ScreenID.SCHEDULE)
)

object ScreenID {
    const val HOME = 0
    const val CREATE = 1
    const val ARCHIVE = 2
    const val SCHEDULE = 3
}

@Composable
fun NavigationComponents(
    selectedScreenID: Int,
    updateSelected: (screenID: Int, newTitle: String) -> Unit
){
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ){
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
fun ScreenComponents(
    selectedScreenID: Int,
    updateSelected: (screenID: Int, newTitle: String) -> Unit
){
    when (selectedScreenID) {
        ScreenID.HOME -> HomeScreen()
        ScreenID.CREATE -> CreateScreen()
        ScreenID.ARCHIVE -> ArchiveScreen()
        ScreenID.SCHEDULE -> GeoLocationScreen()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScaffold(){
    var selectedScreenID by remember { mutableStateOf(ScreenID.HOME) }
    var title by remember {
        mutableStateOf("Home") //TODO tidy this
    }
    fun updatedSelectedID(id: Int, newTitle: String){
        //Note: this is a convenience function
        selectedScreenID = id
        title = newTitle
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                title = { Text(title, style = MaterialTheme.typography.headlineLarge) })
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
                .fillMaxSize().padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top)
            {
                Spacer(modifier = Modifier.size(10.dp))
                ScreenComponents(selectedScreenID = selectedScreenID, updateSelected = ::updatedSelectedID)
                Spacer(modifier = Modifier.size(10.dp))
            }
        })
}




