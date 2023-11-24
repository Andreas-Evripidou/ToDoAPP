package com.example.studenttodo.ui.composables

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.services.GeoLocationService
import com.example.studenttodo.viewmodels.LocationViewModel


@Composable
fun GeoLocationScreen(){
    Log.i("geolocation", "Location Screen")
    val locationViewModel = viewModel<LocationViewModel>()

    if (locationViewModel.valid){
        CoarseLocation(
            lat = locationViewModel.latitude.toString(),
            lon = locationViewModel.longitude.toString()
        )
    } else {
        Text("LOCATION UNAVAILABLE")
    }
}

fun openMaps(context: Context, lat: String, lon: String) {
    val intentUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(5 m)")
    val intent = Intent(Intent.ACTION_VIEW, intentUri)
    intent.setPackage("com.google.android.apps.maps")
    startActivity(context, intent, null)
}

@Composable
fun CoarseLocation(lat: String, lon: String) {
    val context = LocalContext.current
    Column {
        Text("Latitude: $lat")
        Text("Longitude: $lon")
        Button(
            onClick = {
                openMaps(context,lat,lon)
            }) { Text(text = "Open Map")}
    }
    
}
