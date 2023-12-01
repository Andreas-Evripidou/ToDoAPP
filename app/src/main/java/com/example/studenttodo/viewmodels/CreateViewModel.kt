package com.example.studenttodo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studenttodo.data.ToDoDatabase
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.launch

class CreateViewModel (app:Application): AndroidViewModel(app){
    val context = getApplication<Application>().applicationContext
    private
    val dao = ToDoDatabase.getDB(context).todoDao()
    val todos = dao.getActiveTodos()

    fun createToDo (toDo: ToDoEntity) = viewModelScope.launch {
        dao.insert(toDo)
    }

    fun createModule(module: ModuleEntity) {

    }
}