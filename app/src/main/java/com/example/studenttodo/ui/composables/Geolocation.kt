package com.example.studenttodo.ui.composables

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.services.GeoLocationService
import com.example.studenttodo.ui.theme.StudentToDoTheme
import com.example.studenttodo.viewmodels.LocationViewModel


@Composable
fun GeoLocationScreen(){
    val locationViewModel = viewModel<LocationViewModel>()
    GeoLocationService.LocationViewModel = locationViewModel
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context. LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        val location =
            locationManager. getLastKnownLocation(LocationManager.GPS_PROVIDER)
        CoarseLocation(
            location?.latitude.toString(),
            location?.longitude.toString())
    } else {
        Text("COARSE LOCATION UNAVAILABLE")
    }
}

@Composable
fun CoarseLocation(lat: String, lon: String) {
    Column {
        Text("Latitude: $lat")
        Text("Longitude: $lon")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudentToDoTheme {
        CoarseLocation("99.99", "-99.99")
    }
}
