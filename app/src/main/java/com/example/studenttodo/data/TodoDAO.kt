package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studenttodo.entities.ToDoEntity

@Dao
interface TodoDAO {
    @Insert
    suspend fun insert(key: ToDoEntity)
    @Update
    suspend fun update(key: ToDoEntity)
    @Delete
    suspend fun delete(key: ToDoEntity)
//    @Query("SELECT * from " + ToDoEntity.TABLE_NAME + " WHERE id = :id")
//    fun getTodo(id: Int): ToDoEntity
//    @Query("SELECT * from " + ToDoEntity.TABLE_NAME) // Todo:  + " ORDER BY priority ASC"
//    fun getAllTodos(): List<ToDoEntity>
}