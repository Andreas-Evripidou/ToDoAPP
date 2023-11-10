package com.example.studenttodo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun HomeScreen (name: String, modifier: Modifier = Modifier) {
/* TODO displays a list of tasks
 * TODO Colour based on priority ability to mark as completed
 * TODO Click to open page to update/view
 * TODO ordered by date/priority
 */
    Text(
            text = "Hello $name!",
            modifier = modifier
        )
    (1..99).forEach {// once connected to database todo 1..datbase size
        dispTasks()//(databseItem[])
    }
    }

@Composable
fun dispTasks () {

}