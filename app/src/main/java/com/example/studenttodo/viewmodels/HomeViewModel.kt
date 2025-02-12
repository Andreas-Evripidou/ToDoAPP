package com.example.studenttodo.viewmodels

import android.app.Application
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.launch

internal class HomeViewModel (app: Application): AndroidViewModel(app){
    val context = getApplication<Application>().applicationContext
    private
    val dao = ToDoDatabase.getDB(context).todoDao()
    val todos = dao.getActiveTodos()

    fun deleteToDo(toDo: ToDoEntity) = viewModelScope.launch {
        dao.delete(toDo)
    }

    fun getBorderColor(toDo: ToDoEntity): Int {
        var borderColor = when (toDo.priority){
            1 ->  Color.GREEN
            2 ->  Color.YELLOW
            3 -> Color.RED
            else ->{
                Color.WHITE
            }
        }
        return borderColor
    }

    fun viewMore (toDo: ToDoEntity) = viewModelScope.launch {
        //TODO View more details
    }
    fun archiveToDo(toDo: ToDoEntity) = viewModelScope.launch {
        val toDo = ToDoEntity(
            id = toDo.id,
            title = toDo.title,
            reminderTime = toDo.reminderTime,
            reminderDate = toDo.reminderDate,
            latitude = toDo.latitude,
            priority = toDo.priority,
            longitude = toDo.longitude,
            range = toDo.range,
            status = 1,
            description = toDo.description,
            picture = toDo.picture,
            createdLatitude = toDo.createdLatitude,
            createdLongitude = toDo.createdLongitude,
            createdDate = toDo.createdDate,
            createdTime = toDo.createdTime,
            moduleCode = toDo.moduleCode
        )

        dao.update(toDo)

        val text = "To do Archived!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
    }

}