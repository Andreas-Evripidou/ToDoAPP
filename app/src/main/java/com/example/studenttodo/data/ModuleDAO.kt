package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.studenttodo.entities.ModuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleDAO {
    @Insert
    suspend fun insert(key: ModuleEntity)
    @Update
    suspend fun update(key: ModuleEntity): Int
    @Delete
    suspend fun delete(key: ModuleEntity)

    @Transaction
    suspend fun updateOrInsert(module: ModuleEntity) {
        val rowsUpdated = update(module)
        if (rowsUpdated == 0) {
            insert(module)
        }
    }
// Todo:
//    @Query("SELECT * from " + ModuleEntity.TABLE_NAME + " WHERE id = :id")
//    fun getModule(id: String): ModuleEntity
    @Query("SELECT * from " + ModuleEntity.TABLE_NAME)
    fun getAll(): Flow<List<ModuleEntity>>
}