package com.example.studenttodo.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.entities.ToDoEntity
import com.example.studenttodo.entities.TimetableEntity
import com.example.studenttodo.viewmodels.CreateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime
import kotlin.coroutines.CoroutineContext

@Database(entities = [ToDoEntity::class,  ModuleEntity::class, TimetableEntity::class], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDAO

    abstract fun moduleDAO(): ModuleDAO
    abstract fun timetableDAO(): TimetableDAO

    companion object {
        private const val DB_NAME = "todo_db"

        var firstrun = false

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context,
                ToDoDatabase::class.java,
                DB_NAME ).fallbackToDestructiveMigration().addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        Log.d("Database","Create")
                        super.onCreate(db)
                        // Insert Templates
                        createTemplates(context)
                    }
                }).build()

        @Volatile private var thisDB: ToDoDatabase? = null

        fun getDB(context: Context): ToDoDatabase =
            thisDB ?: synchronized(this) {
                thisDB ?: buildDatabase(context).also {
                    thisDB = it
                }
            }

        fun createTemplates(context: Context) {
            Log.d("Database","Templates")
            val moduleCOM31007 = ModuleEntity(
                moduleCode = "COM31007",
                moduleTitle = "Software Development for Mobile Devices",
            )

            val todoTeamAssignment = ToDoEntity(
                title = "Team Assignment",
                reminderTime =  LocalTime.now(),
                reminderDate = LocalDate.now(),
                latitude = "53.38178263399827",
                priority = 1,
                longitude = "-1.4815964748271253",
                range = "15",
                status = 0,
                description = "Mobile Application Development in a Team",
                picture = "",
                createdLatitude = "53.38178263399827",
                createdLongitude = "-1.4815964748271253",
                createdDate = LocalDate.now(),
                createdTime = LocalTime.now(),
                moduleCode = "COM31007"
            )

            GlobalScope.launch {
                Log.d("Database","Insert Templates")
                var moduleDao  = getDB(context).moduleDAO()
                moduleDao.updateOrInsert(moduleCOM31007)
                val todoDao = getDB(context).todoDao()
                todoDao.updateOrInsert(todoTeamAssignment)
            }



        }
    }
}