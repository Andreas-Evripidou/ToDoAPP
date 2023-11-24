package com.example.studenttodo.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.Transaction
import androidx.room.Update
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDAO {
    @Insert
    suspend fun insert(key: TimetableEntity)
    @Update
    suspend fun update(key: TimetableEntity): Int
    @Delete
    suspend fun delete(key: TimetableEntity)

    @Transaction
    suspend fun updateOrInsert(timetable: TimetableEntity) {
        val rowsUpdated = update(timetable)
        if (rowsUpdated == 0) {
            insert(timetable)
        }
    }
//    @Query("SELECT * from " + TimetableEntity.TABLE_NAME + " WHERE id = :id")
//    fun getTodo(id: Int): TimetableEntity
    @Query("SELECT * from " + TimetableEntity.TABLE_NAME)
    fun getAllTimeTable(): Flow<List<TimetableEntity>>

}