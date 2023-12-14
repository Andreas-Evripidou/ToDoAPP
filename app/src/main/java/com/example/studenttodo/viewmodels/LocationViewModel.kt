package com.example.studenttodo.viewmodels

import android.app.Application
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.postDelayed
import androidx.lifecycle.AndroidViewModel
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.services.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocationViewModel (app: Application): AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    val timetableDao = ToDoDatabase.getDB(context).timetableDAO()
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
        // Set the new location
        location = newLocation

        // Update latitude and longitude, setting them to null if the location is null
        latitude = location?.latitude ?: null
        longitude = location?.longitude ?: null

        // Update the validity status based on the new location
        valid = isValid()
    }

    /**
     * Updates the current location and triggers the automatic module selection process in a coroutine.
     *
     * @param newLocation The new location to set.
     */
    fun updateLocation(newLocation: Location) {
        // Set the new location and trigger automatic module selection in a coroutine
        _setLocation(newLocation)
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            automaticModuleSelection()
            locationNotif()
        }

    }

    //Displays notification when the user enters the location of a reminder
    //to prevent code executing quickly in succession and sending multiple notifs
    private var lastExecutionTimeMillis = 0L
    private val cooldownMillis = 2000L
    suspend fun locationNotif(){
        val dao = ToDoDatabase.getDB(context).todoDao()
        val todos = dao.getActiveTodosNoti()
        todos.forEach { x ->
            val longi = x.longitude
            val lati = x.latitude
            val range = x.range
            val title = x.title
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - lastExecutionTimeMillis >= cooldownMillis) {
                // Update the last execution time
                lastExecutionTimeMillis = currentTimeMillis

                //checks if user is in area of to do and a notification hasn't been sent yet
                if (isInArea(longi, lati, range) && x.atLocationNotified == 0) {
                    print("operating")
                    Handler(Looper.getMainLooper()).post(){
                        val text = "Reminder: you have entered the location of  ${title}!"
                        val duration = Toast.LENGTH_LONG
                        val toast = Toast.makeText(context,text,duration)
                        toast.show()
                    }
                    x.atLocationNotified = 1
                    dao.update(x)
                    //enables notifications to be sent again once user leaves to do area
                }else if(!isInArea(longi,lati,range) && x.atLocationNotified == 1){
                    x.atLocationNotified = 0
                    dao.update(x)
                }
            }

        }
    }
    /**
     * Retrieves the currently detected module.
     *
     * @return The module code of the currently detected module.
     */
    fun detectedModule(): String {
        return detectedModule
    }

    /**
     * Checks if the current location is within a specified geographical area.
     *
     * @param areaLon The longitude of the center of the area.
     * @param areaLat The latitude of the center of the area.
     * @param areaRadius The radius of the area in meters.
     * @return True if the current location is within the specified area, false otherwise.
     */
    private fun isInArea(areaLon: String, areaLat: String, areaRadius: String): Boolean {
        // Check if the location is valid before proceeding with calculations
        if (valid) {
            return try {
                // Get the current latitude and longitude
                val lat = latitude ?: 0.0
                val lon = longitude ?: 0.0

                // Create a Location object for the center of the given area
                val startPoint = Location("givenLocation")
                startPoint.latitude = areaLat.toDouble()
                startPoint.longitude = areaLon.toDouble()

                // Create a Location object for the current location
                val endPoint = Location("currentLocation")
                endPoint.latitude = lat
                endPoint.longitude = lon

                // Calculate the distance between the two locations
                val distance = startPoint.distanceTo(endPoint).toDouble()
                // Check if the distance is within the specified area radius
                distance <= areaRadius.toDouble()
            } catch (e: Exception) {
                // If there any exceptions return false
                false
            }
        }

        // Return false if the location is not valid
        return false
    }


    fun invalidate() {
        _setLocation(null)
    }

    /**
     * Checks if the current location is valid.
     *
     * @return True if the location is not null, indicating a valid location; false otherwise.
     */
    private fun isValid(): Boolean {
        // Check if the location is not null, indicating a valid location
        return (location != null)
    }

    /**
     * This function performs automatic module selection based on the current day,
     * time, and location. It queries the timetable for the current time and day,
     * checks if the device is within the specified radius of any module's location,
     * and updates the detectedModule accordingly.
     */
    private suspend fun automaticModuleSelection() {
        // Get the current day of the week as a string (e.g., "MONDAY")
        val today = LocalDate.now().dayOfWeek.toString()

        // Initialize a mutable state variable to track whether a module is found
        var foundModule = mutableStateOf(false)

        // Formatting current time as "hh:mm"
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTimeString = LocalTime.now().format(formatter)
        val formattedTime = LocalTime.parse(formattedTimeString)

        // Convert the day string to lowercase and capitalize the first letter (e.g., "Monday")
        val formattedDay = today.lowercase().replaceFirstChar { it.uppercase() }

        // Iterate over the timetable rows for the current time and day
        timetableDao.getTimetableRowsByTime(formattedTime, formattedDay).forEach { item ->
            // Check if the current location is within the specified radius of the timetable item
            if (isInArea(item.long, item.lat, item.radius)) {
                // Set foundModule to true if a module is detected
                foundModule.value = true

                // If the module was not detected before, set the detectedModule to the current module code
                // (If there are more than one modules detected, only use the first one)
                if (detectedModule == "") {
                    detectedModule = item.moduleCode
                }
            }

            // If no module was found, reset the detectedModule
            if (!foundModule.value) {
                detectedModule = ""
            }
        }
    }
}