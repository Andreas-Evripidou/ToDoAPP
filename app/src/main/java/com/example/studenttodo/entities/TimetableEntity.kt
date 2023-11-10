package com.example.studenttodo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.DayOfWeek

@Entity(TimetableEntity.TABLE_NAME)
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: String, // Todo: change to Enum or DayOfWeek
    val time: String, // Todo: Time
    val moduleCode: String //Todo: Foreign Key
) {
    companion object {
        const val TABLE_NAME = "Timetable"
    }
}
