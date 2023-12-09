package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.studenttodo.entities.TimetableEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

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

    @Query("SELECT * FROM ${TimetableEntity.TABLE_NAME} WHERE startTime < :givenTime AND endTime > :givenTime AND day = :giveDay AND status = 0")
    suspend fun getTimetableRowsByTime(givenTime: LocalTime, giveDay: String): List<TimetableEntity>

}