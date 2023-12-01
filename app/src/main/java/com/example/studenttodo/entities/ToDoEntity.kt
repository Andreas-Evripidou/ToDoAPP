package com.example.studenttodo.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = ToDoEntity.TABLE_NAME, foreignKeys = [ForeignKey(entity = ModuleEntity::class,
    parentColumns = ["moduleCode"],
    childColumns = ["moduleCode"],
    onDelete = ForeignKey.NO_ACTION)])
data class ToDoEntity  (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val reminderDate: LocalDate,
    val reminderTime: LocalTime,
    val priority: Int,
    val latitude: String,
    val longitude: String,
    val range: String, // Todo: Maybe Float/Double
    val status: Int,
    val description: String,
    val picture: String, // Todo: handle Pictures
    val createdLatitude: String,
    val createdLongitude: String,
    val createdDate: LocalDate = LocalDate.now(),
    val createdTime: LocalTime = LocalTime.now(),
    val moduleCode: String
) {
    companion object {
        const val TABLE_NAME = "Todos"
    }

    val reminderDateFormatted : String
        get() = reminderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val createdDateFormatted :  String
        get() = createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val createdTimeFormatted : String
        get() = createdTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    val reminderTimeFormatted : String
        get() = reminderTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}