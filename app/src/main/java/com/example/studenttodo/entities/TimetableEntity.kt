package com.example.studenttodo.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalTime
import kotlin.reflect.KClass

@Entity(tableName = TimetableEntity.TABLE_NAME, foreignKeys = [ForeignKey(entity = ModuleEntity::class,
                                                            parentColumns = ["moduleCode"],
                                                            childColumns = ["moduleCode"],
                                                            onDelete = ForeignKey.NO_ACTION)])
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: String, // Todo: change to Enum or DayOfWeek
    val startTime: LocalTime,
    val endTime: LocalTime,
    val moduleCode: String,
    val status: Int
) {
    companion object {
        const val TABLE_NAME = "Timetable"
    }
}
