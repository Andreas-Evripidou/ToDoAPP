package com.example.studenttodo.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.TimetableDAO
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

internal class ScheduleViewModel (app: Application): AndroidViewModel(app){
    val context = getApplication<Application>().applicationContext
    private
    val dao = ToDoDatabase.getDB(context).timetableDAO()
    val timetable = dao.getAllTimeTable()

    fun viewMore (timetable: TimetableEntity) = viewModelScope.launch {
        //TODO View more details
    }
    fun editTimetable(timetable: TimetableEntity) = viewModelScope.launch {
        dao.update(timetable)

        val text = "Timetable Item Updated!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
    }
    fun createTimetable(timetable: TimetableEntity) = viewModelScope.launch {
        dao.insert(timetable)

        val text = "Timetable Item Created!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
    }
    fun deleteTimetable(timetable: TimetableEntity) = viewModelScope.launch {
        val timetable = TimetableEntity(
            id = timetable.id,
            day = timetable.day,
            startTime = timetable.startTime,
            endTime = timetable.endTime,
            moduleCode = timetable.moduleCode,
            lat = timetable.lat,
            long = timetable.long,
            radius = timetable.radius,
            itemType = timetable.itemType,
            status = 1
        )
        dao.update(timetable)

        val text = "Timetable Item Deleted!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
    }


}