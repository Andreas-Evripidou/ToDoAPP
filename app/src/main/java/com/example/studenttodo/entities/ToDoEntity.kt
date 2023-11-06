package com.example.studenttodo.entities

import java.sql.Time
import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(ToDoEntity.TABLE_NAME)
data class ToDoEntity  (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val reminderDate: Date,
    val reminderTime: Time,
    val priority: String, // Todo: Enum
    val latitude: String,
    val longitude: String,
    val range: String, // Todo: Maybe Float/Double
    val status: String // Todo: Enum
) {
    companion object {
        const val TABLE_NAME = "Todos"
    }
}