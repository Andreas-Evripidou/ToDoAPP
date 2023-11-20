package com.example.studenttodo.viewmodels

import android.app.Application
import android.location.Location
import android.location.LocationManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(app: Application): AndroidViewModel(app) {
    private var location = Location(LocationManager.GPS_PROVIDER)

    fun updateLocation(newLocation: Location){
        this.location = newLocation
    }
}