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
    fun insert(key: ModuleEntity)
    @Update
    fun update(key: ModuleEntity)
    @Delete
    fun delete(key: ModuleEntity)
    @Query("SELECT * from " + ModuleEntity.TABLE_NAME + " WHERE id = :id")
    fun getModule(id: String): ModuleEntity
    @Query("SELECT * from " + ModuleEntity.TABLE_NAME)
    fun getAllModules(): List<ModuleEntity>
}