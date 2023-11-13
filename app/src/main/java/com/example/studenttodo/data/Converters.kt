package com.example.studenttodo.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.util.*

class Converters {
    @TypeConverter
    fun fromDateString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it)}
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromTimestamp(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it)}
    }

    @TypeConverter
    fun timeToTimestamp(time: LocalTime?): String? {
        return time?.toString()
    }

}