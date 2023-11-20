package com.example.studenttodo

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.studenttodo.ui.composables.CoarseLocation
import com.example.studenttodo.ui.composables.NavigationScaffold
import com.example.studenttodo.ui.theme.StudentToDoTheme
import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.services.GeoLocationService
import com.example.studenttodo.viewmodels.LocationViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermission()){
            requestFineLocationPermission()
        }
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

    private val GPS_LOCATION_PERMISSION_REQUEST = 1
    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            GPS_LOCATION_PERMISSION_REQUEST,
        )
    }

    private fun hasPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
                )
    }

    override fun onResume() {
        super.onResume()
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        @SuppressLint("MissingPermission")
        if (hasPermission()){
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if ( location!= null){
                GeoLocationService.updateLatestLocation(location)
            }
        }
    }
    override fun onPause(){
        super.onPause()
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(GeoLocationService)
    }

}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    StudentToDoTheme {
        NavigationScaffold()
    }
}