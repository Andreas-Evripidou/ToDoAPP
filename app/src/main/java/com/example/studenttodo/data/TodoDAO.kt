package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.ToDoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDAO {
    @Insert
    suspend fun insert(key: ToDoEntity)

    @Update
    suspend fun update(key: ToDoEntity): Int

    @Delete
    suspend fun delete(key: ToDoEntity)

    @Transaction
    suspend fun updateOrInsert(todo: ToDoEntity) {
        val rowsUpdated = update(todo)
        if (rowsUpdated == 0) {
            insert(todo)
        }
    }
//    @Query("SELECT * from " + ToDoEntity.TABLE_NAME + " WHERE id = :id")
//    fun getTodo(id: Int): ToDoEntity
    @Query("SELECT * from " + ToDoEntity.TABLE_NAME) // Todo:  + " ORDER BY priority ASC"
    fun getAllTodos(): Flow<List<ToDoEntity>>

    @Query("SELECT * from " + ToDoEntity.TABLE_NAME + " WHERE status = 1") // Todo:  + " ORDER BY priority ASC"
    fun getArchiveTodos(): Flow<List<ToDoEntity>>

    @Query("SELECT * from " + ToDoEntity.TABLE_NAME + " WHERE status = 0" + " ORDER BY priority DESC")
    fun getActiveTodos(): Flow<List<ToDoEntity>>

    @Query("SELECT * from " + ToDoEntity.TABLE_NAME + " WHERE status = 0" + " ORDER BY priority DESC")
    fun getActiveTodosNoti(): List<ToDoEntity>
}