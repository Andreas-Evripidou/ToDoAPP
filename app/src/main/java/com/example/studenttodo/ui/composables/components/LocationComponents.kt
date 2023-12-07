package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
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
    var locationradius by remember { mutableStateOf(radius) }
    val text = if (edit) "Edit Location" else "Add Location"
    var showLocation by remember { mutableStateOf(lon.isNotEmpty() && lat.isNotEmpty() && radius.isNotEmpty()) }


    // If location is optional, display the checkbox
    if (optional) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showLocation = !showLocation }
        ) {
            Checkbox(
                checked = showLocation,
                onCheckedChange = { showLocation = it })
            Text(text)
        }
    // Else, always show the location options
    } else {
        showLocation = true
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
                locationradius)
        }
    }

    // If showing location and the current location is valid,
    // show the use current location checkbox
    if (showLocation && locationViewModel.valid){
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
    if (showLocation && !useCurrentLocation) {
        Text(text = "Longitude", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it
                            updateSelectedLoc(longitude, latitude, locationradius)},
            label = { Text("longitude") })

        Text(text = "Latitude", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it
                updateSelectedLoc(longitude, latitude, locationradius)},
            label = { Text("latitude") })
    }
    // If showing locating, display the radius text field
    if (showLocation) {
        Text(text = "Location Radius", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = locationradius,
            onValueChange = { locationradius = it
                updateSelectedLoc(longitude, latitude, locationradius)},
            label = { Text("radius") })
    }

}