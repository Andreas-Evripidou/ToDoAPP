package com.example.studenttodo.viewmodels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class LocationViewModel (app: Application): AndroidViewModel(app) {
    var location:Location? by mutableStateOf<Location?>(null)
        private set
    var latitude by mutableStateOf<Double?>(null)
        private set
    var longitude by mutableStateOf<Double?>(null)
        private set
    var valid by mutableStateOf(false)
        private set
    private var detectedModule by mutableStateOf("")

    private fun _setLocation(newLocation: Location?) {
        location = newLocation
        latitude = location?.latitude ?: null
        longitude = location?.longitude ?: null
        valid = isValid()
    }

    fun updateLocation(newLocation: Location) {
        _setLocation(newLocation)
    }

    fun getDetectedModule(): String {
        return detectedModule
    }
    fun updateDetectedModule(mc: String){
        detectedModule = mc
    }

    fun removeDetectedModule(){
        detectedModule = ""
    }

    fun isInArea(areaLon: String, areaLat: String, areaRadius: String): Boolean {

        if (valid){
            return try {
                val lat = latitude ?: 0.0
                val lon = longitude ?: 0.0

                val startPoint = Location("givenLocation")
                startPoint.latitude = areaLat.toDouble()
                startPoint.longitude = areaLon.toDouble()

                val endPoint = Location("currentLocation")
                endPoint.latitude = lat
                endPoint.longitude = lon

                val distance = startPoint.distanceTo(endPoint).toDouble()

                Log.i("Automatic", "distance: $distance")
                (distance <= areaRadius.toDouble())
            }catch (e: Exception){
                false
            }
        }
        return false
    }

    fun invalidate() {
        _setLocation(null)
    }

    private fun isValid():Boolean {
        return (location != null)
    }
}