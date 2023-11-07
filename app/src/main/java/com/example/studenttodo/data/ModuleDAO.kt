package com.example.studenttodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studenttodo.entities.ModuleEntity

@Dao
interface ModuleDAO {
    @Insert
    suspend fun insert(key: ModuleEntity)
    @Update
    suspend fun update(key: ModuleEntity)
    @Delete
    suspend fun delete(key: ModuleEntity)
//    @Query("SELECT * from " + ModuleEntity.TABLE_NAME + " WHERE id = :id")
//    fun getModule(id: String): ModuleEntity
//    @Query("SELECT * from " + ModuleEntity.TABLE_NAME)
//    fun getAllModules(): List<ModuleEntity>
}