package com.example.studenttodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.entities.TimetableEntity

@Database(entities = [ToDoEntity::class,  ModuleEntity::class, TimetableEntity::class], version = 2, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDAO

    abstract fun moduleDAO(): ModuleDAO
    abstract fun timetableDAO(): TimetableDAO

    companion object {
        private const val DB_NAME = "todo_db"

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context,
                ToDoDatabase::class.java,
                DB_NAME ).fallbackToDestructiveMigration().build()

        @Volatile private var thisDB: ToDoDatabase? = null

        fun getDB(context: Context): ToDoDatabase =
            thisDB ?: synchronized(this) {
                thisDB ?: buildDatabase(context).also { thisDB = it }
            }
    }
}
