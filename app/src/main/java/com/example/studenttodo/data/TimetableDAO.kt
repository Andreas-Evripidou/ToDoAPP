package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studenttodo.entities.TimetableEntity

@Dao
interface TimetableDAO {
    @Insert
    suspend fun insert(key: TimetableEntity)
    @Update
    suspend fun update(key: TimetableEntity)
    @Delete
    suspend fun delete(key: TimetableEntity)
//    @Query("SELECT * from " + TimetableEntity.TABLE_NAME + " WHERE id = :id")
//    fun getTodo(id: Int): TimetableEntity
//    @Query("SELECT * from " + TimetableEntity.TABLE_NAME)
//    fun getAllTodos(): List<TimetableEntity>
}