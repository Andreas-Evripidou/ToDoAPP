package com.example.studenttodo.entities

import java.sql.Time
import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(ToDoEntity.TABLE_NAME)
data class ModuleEntity(
    @PrimaryKey(autoGenerate = false)
    val moduleCode: String, //todo: Change to Enum or ad an int id
    val location: String,
    val moduleTitle: String
) {
    companion object {
        const val TABLE_NAME = "Modules"
    }
}
