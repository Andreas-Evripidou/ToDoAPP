package com.example.studenttodo.ui.composables.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.viewmodels.LocationViewModel
import com.example.studenttodo.viewmodels.ScheduleViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AutomaticModuleSelection(updateSelectedModuleCode: (mc: String) -> Unit) {
    val locationViewModel = viewModel<LocationViewModel>()
    val scheduleViewModel = viewModel<ScheduleViewModel>()
    val context = LocalContext.current

    val today = LocalDate.now().dayOfWeek.toString()
    var foundModule by remember{mutableStateOf(false)}

    LaunchedEffect(key1 = "GET_CURRENT_SCHEDULE_ITEMS", context) {
        scheduleViewModel.findCurrentScheduleItem(LocalTime.now(), today).forEach {
            item ->

            if (locationViewModel.isInArea(item.long, item.lat, item.radius)){

                foundModule = true
                // If the module was not detected before
                if (locationViewModel.detectedModule() == ""){
                    locationViewModel.updateDetectedModule(item.moduleCode)
                    updateSelectedModuleCode(item.moduleCode)

                    val text = "You are in ${item.moduleCode} ${item.itemType}!"
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(context, text, duration) // in Activity
                    toast.show()
                }
            }
        }

        // If no module was found, reset the detectedModule
        if (!foundModule){
            locationViewModel.removeDetectedModule()
        }
    }


}