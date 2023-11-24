package com.example.studenttodo.viewmodels

import android.app.Application
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel (app: Application): AndroidViewModel(app) {
    // TODO synchronize/lock
    var tracking by mutableStateOf(false)
    var location:Location? by mutableStateOf<Location?>(null)
        private set
    var latitude by mutableStateOf<Double?>(null)
        private set
    var longitude by mutableStateOf<Double?>(null)
        private set
    var valid by mutableStateOf(false)
        private set

    private fun _setLocation(newLocation: Location?) {
        location = newLocation
        latitude = location?.latitude ?: null
        longitude = location?.longitude ?: null
        valid = valid()
    }

    fun updateLocation(newLocation: Location) {
        _setLocation(newLocation)
    }

    fun invalidate() {
        _setLocation(null)
    }

    fun valid():Boolean {
        return (location != null)
    }
}