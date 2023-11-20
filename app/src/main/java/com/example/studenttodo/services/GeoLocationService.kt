package com.example.studenttodo.services

import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.example.studenttodo.viewmodels.LocationViewModel

object GeoLocationService: LocationListener {
    var LocationViewModel: LocationViewModel? = null
    override fun onLocationChanged(newLocation: Location) {
        LocationViewModel?.updateLocation(newLocation)
        Log.i("geolocation", "Location updated")
    }

    fun updateLatestLocation(latestLocation: Location) {
        LocationViewModel?.updateLocation(latestLocation)
        Log.i("geolocation", "Location set to latest")
    }
}