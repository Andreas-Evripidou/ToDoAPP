package com.example.studenttodo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class CreateViewModel (app:Application): AndroidViewModel(app){
    val context = getApplication<Application>().applicationContext
    private
    val dao = ToDoDatabase.getDB(context).todoDao()
    val todos = dao.getActiveTodos()

    val moduledao = ToDoDatabase.getDB(context).moduleDAO()
    val modules = moduledao.getAll()

    fun createToDo (toDo: ToDoEntity) = viewModelScope.launch {
        dao.insert(toDo)
    }
    fun createOrUpdateToDo (toDo: ToDoEntity) = viewModelScope.launch {
        dao.updateOrInsert(toDo)
    }
    fun emptyTodo(): ToDoEntity {
        return ToDoEntity(
            id = 0, // Assuming id is an integer, you can set it accordingly
            title = "",
            reminderTime =  LocalTime.now(),
            reminderDate = LocalDate.now(),
            latitude = "",
            priority = 1,
            longitude = "",
            range = "",
            status = 1,
            description = "",
            picture = "",
            createdLatitude = "",
            createdLongitude = "",
            createdDate = LocalDate.now(),
            createdTime = LocalTime.now(),
            moduleCode = ""
        )
    }
    fun createModule(module: ModuleEntity) = viewModelScope.launch {
        moduledao.updateOrInsert(module)
    }
}
