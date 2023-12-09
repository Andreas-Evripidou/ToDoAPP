package com.example.studenttodo.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = TimetableEntity.TABLE_NAME, foreignKeys = [ForeignKey(entity = ModuleEntity::class,
    parentColumns = ["moduleCode"],
    childColumns = ["moduleCode"],
    onDelete = ForeignKey.CASCADE)])
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: String, // Todo: change to Enum or DayOfWeek
    val startTime: LocalTime,
    val endTime: LocalTime,
    val moduleCode: String,
    val itemType: String,
    val lat: String,
    val long: String,
    val radius: String,
    val status: Int
) {
    companion object {
        const val TABLE_NAME = "Timetable"
    }
}
