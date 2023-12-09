package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.viewmodels.LocationViewModel

/*
* Composable function to display
* Location options
* */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
// ToDo: Since there are more than 3 parameters, we should use an object
fun SelectLocation(
    lon: String,
    lat: String,
    radius: String,
    optional: Boolean= true,
    edit: Boolean= false,
    updateSelectedLoc: (lon: String, lat: String, radius: String) -> Unit){

    var useCurrentLocation by remember { mutableStateOf(false) }
    val locationViewModel = viewModel<LocationViewModel>()

    var longitude by remember { mutableStateOf(lon) }
    var latitude by remember { mutableStateOf(lat) }
    var locationRadius by remember { mutableStateOf(radius) }
    val text = if (edit) "Edit Location" else "Add Location"
    val showLocation = remember { mutableStateOf(lon.isNotEmpty() && lat.isNotEmpty() && radius.isNotEmpty()) }


    // If location is optional, display the checkbox
    if (optional) {
        CustomSubMenu(text, showLocation)
        
        Spacer(modifier = Modifier.size(4.dp))
    // Else, always show the location options
    } else {
        showLocation.value = true
    }

    fun onChecked(){

        // If using current location,
        // updated the Selected location to the current one
        if(useCurrentLocation){
            longitude = locationViewModel.longitude.toString()
            latitude = locationViewModel.latitude.toString()
            updateSelectedLoc(
                longitude,
                latitude,
                locationRadius)
        }
    }

    // If showing location and the current location is valid,
    // show the use current location checkbox
    if (showLocation.value && locationViewModel.valid){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { useCurrentLocation = !useCurrentLocation
                onChecked()
            }
        ) {
            Checkbox(
                checked = useCurrentLocation,
                onCheckedChange = {
                    useCurrentLocation = it
                    onChecked()
                })
            Text("Use current location")
        }
    }

    // If showing location but not using the current location
    // display the text fields for lon and lat
    if (showLocation.value && !useCurrentLocation) {

        Text(text = "Longitude:", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            modifier = Modifier.width(200.dp),
            value = longitude,
            onValueChange = { newInput ->

                if ( isValidLongitude(newInput) ) {
                    longitude = newInput.take(17)
                    updateSelectedLoc(longitude, latitude, locationRadius)
                } },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number),
            label = { Text("longitude") })

        Text(text = "Latitude:", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            modifier = Modifier.width(200.dp),
            value = latitude,
            onValueChange = { newInput ->

                if ( isValidLatitude(newInput) ) {
                    latitude = newInput.take(17)
                    updateSelectedLoc(longitude, latitude, locationRadius)
                } },
            label = { Text("latitude") })
    }
    // If showing locating, display the radius text field
    if (showLocation.value) {
        Text(text = "Location Radius:", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            modifier = Modifier.width(110.dp),
            value = locationRadius,
            onValueChange = { newInput ->

                if ( newInput == "" || newInput.toIntOrNull() in 0..1000 ) {
                    locationRadius = newInput
                    updateSelectedLoc(longitude, latitude, locationRadius)
                } },
            label = { Text("in meters") })
    }

}

fun isValidLongitude(value: String): Boolean {
    // Allow empty textField
    if (value in  setOf("", "-")) {return true}
    return try {
        val longitude = value.toDouble()
        longitude in -180.0..180.0
    } catch (e: NumberFormatException) {
        false
    }
}

fun isValidLatitude(value: String): Boolean {
    // Allow empty textField
    if (value in  setOf("", "-")) {return true}

    return try {
        val latitude = value.toDouble()
        latitude in -90.0..90.0
    } catch (e: NumberFormatException) {
        false
    }
}