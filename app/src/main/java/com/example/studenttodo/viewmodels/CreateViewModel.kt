package com.example.studenttodo.viewmodels

import android.app.Application
import android.widget.Toast
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
    fun createOrUpdateToDo (toDo: ToDoEntity, edit: Boolean) = viewModelScope.launch {
        dao.updateOrInsert(toDo)

        val text = if (edit) "Todo Updated!" else "Todo Created!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, text, duration) // in Activity
        toast.show()
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
            status = 0,
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
