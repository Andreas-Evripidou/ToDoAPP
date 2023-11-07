package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studenttodo.entities.TodoDetailsEntity

@Dao
interface TodoDetailsDAO {
    @Insert
    fun insert(key: TodoDetailsEntity)
    @Update
    fun update(key: TodoDetailsEntity)
    @Delete
    fun delete(key: TodoDetailsEntity)
    @Query("SELECT * from " + TodoDetailsEntity.TABLE_NAME + " WHERE id = :id")
    fun getTodoDetails(id: Int): TodoDetailsEntity
}