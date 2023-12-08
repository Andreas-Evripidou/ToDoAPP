package com.example.studenttodo.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.TimetableEntity
import kotlinx.coroutines.launch
import java.time.LocalTime

internal class ScheduleViewModel (app: Application): AndroidViewModel(app){
    private val context = getApplication<Application>().applicationContext
    val dao = ToDoDatabase.getDB(context).timetableDAO()
    val timetable = dao.getAllTimeTable()

    fun updateOrInsert(timetable: TimetableEntity) = viewModelScope.launch {
        dao.updateOrInsert(timetable)

        val text = "Timetable Item Updated!"
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