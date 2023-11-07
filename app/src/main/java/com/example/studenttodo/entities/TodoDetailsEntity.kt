package com.example.studenttodo.entities

import java.sql.Time
import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(ToDoEntity.TABLE_NAME)
data class TodoDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val picture: String, // Todo: handle Pictures
    val createdLatitude: String,
    val createdLongitude: String,
    val createdDate: Date,
    val createdTime: Time,
    val moduleCode: String, // Todo: Enum
) {
    companion object {
        const val TABLE_NAME = "TodoDetails"
    }
}