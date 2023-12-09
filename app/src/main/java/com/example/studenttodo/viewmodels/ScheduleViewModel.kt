package com.example.studenttodo.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.TimetableEntity
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal class ScheduleViewModel (app: Application): AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    val dao = ToDoDatabase.getDB(context).timetableDAO()
    val timetable = dao.getAllTimeTable()

    suspend fun findCurrentScheduleItem(timeNow: LocalTime, day: String): List<TimetableEntity> {

        // Formatting current time as "hh:mm"
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTimeString = timeNow.format(formatter)
        val formattedTime = LocalTime.parse(formattedTimeString)
        val formattedDay = day.lowercase().replaceFirstChar { it.uppercase() }

        return dao.getTimetableRowsByTime(givenTime = formattedTime, giveDay = formattedDay)
    }



    fun updateOrInsert(timetable: TimetableEntity) = viewModelScope.launch {
        dao.updateOrInsert(timetable)

        val text = "Timetable Updated!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
    }
    fun emptyTimetableItem(day: String = ""): TimetableEntity {
        return TimetableEntity(
            day = day,
            startTime = LocalTime.now(),
            endTime = LocalTime.now(),
            moduleCode = "",
            lat = "",
            long = "",
            radius = "",
            itemType = "Lecture",
            status = 0
        )
    }
    fun deleteTimetable(timetableItem: TimetableEntity) = viewModelScope.launch {
        val newTimetableItem = TimetableEntity(
            id = timetableItem.id,
            day = timetableItem.day,
            startTime = timetableItem.startTime,
            endTime = timetableItem.endTime,
            moduleCode = timetableItem.moduleCode,
            lat = timetableItem.lat,
            long = timetableItem.long,
            radius = timetableItem.radius,
            itemType = timetableItem.itemType,
            status = 1
        )
        dao.update(newTimetableItem)

        val text = "Timetable Item Deleted!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
    }


}