package com.example.studenttodo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.launch

internal class ArchiveViewModel (app: Application): AndroidViewModel(app){
    val context = getApplication<Application>().applicationContext
            private
    val dao = ToDoDatabase.getDB(context).todoDao()
    val todos = dao.getArchiveTodos()

    fun deleteToDo(toDo: ToDoEntity) = viewModelScope.launch {

        val toDo = ToDoEntity(
            id = toDo.id,
            title = toDo.title,
            reminderTime = toDo.reminderTime,
            reminderDate = toDo.reminderDate,
            latitude = toDo.latitude,
            priority = toDo.priority,
            longitude = toDo.longitude,
            range = toDo.range,
            createdDate = toDo.createdDate,
            createdLatitude = toDo.createdLatitude,
            createdLongitude = toDo.createdLongitude,
            createdTime = toDo.createdTime,
            description = toDo.description,
            moduleCode = toDo.moduleCode,
            picture = toDo.picture,
            status = 2
        )
        dao.update(toDo)
    }

    fun restoreToDo(toDo: ToDoEntity) = viewModelScope.launch {
        val toDo = ToDoEntity(
            id = toDo.id,
            title = toDo.title,
            reminderTime = toDo.reminderTime,
            reminderDate = toDo.reminderDate,
            latitude = toDo.latitude,
            priority = toDo.priority,
            longitude = toDo.longitude,
            range = toDo.range,
            createdDate = toDo.createdDate,
            createdLatitude = toDo.createdLatitude,
            createdLongitude = toDo.createdLongitude,
            createdTime = toDo.createdTime,
            description = toDo.description,
            moduleCode = toDo.moduleCode,
            picture = toDo.picture,
            status = 0
        )
        dao.update(toDo)
    }
}
