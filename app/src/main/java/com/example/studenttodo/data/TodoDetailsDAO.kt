package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.TodoDetailsEntity

@Dao
interface TodoDetailsDAO {
    @Insert
    suspend fun insert(key: TodoDetailsEntity)

    @Update
    suspend fun update(key: TodoDetailsEntity): Int

    @Delete
    suspend fun delete(key: TodoDetailsEntity)

    @Transaction
    suspend fun updateOrInsert(todoDetails: TodoDetailsEntity) {
        val rowsUpdated = update(todoDetails)
        if (rowsUpdated == 0) {
            insert(todoDetails)
        }
    }
//    @Query("SELECT * from " + TodoDetailsEntity.TABLE_NAME + " WHERE id = :id")
//    fun getTodoDetails(id: Int): TodoDetailsEntity
}