package com.example.studenttodo.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.studenttodo.data.ToDoDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import com.example.studenttodo.viewmodels.LocationViewModel

class NotificationService : Service() {

    private lateinit var handler: Handler

    private val runnable = object : Runnable {
        override fun run() {
            checkTimeAndPerformAction()
            handler.postDelayed(this, INTERVAL)

        }
    }

    companion object {
        const val INTERVAL: Long = 1000 * 60 // 1 minute interval
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create a new thread for background tasks
        handler = Handler(mainLooper)
        Thread {
            while (true) {
                checkTimeAndPerformAction()
                Thread.sleep(INTERVAL)
            }
        }.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun checkTimeAndPerformAction() {
        val storedTime = "12:30"
        val currentTime = getCurrentTime()
        val currentDate = getCurrentDate()
        val dao = ToDoDatabase.getDB(applicationContext).todoDao()
        val todos = dao.getActiveTodosNoti()

        //looks at the date and time of todos and compares to current
        todos.forEach{ x ->
            val time = x.reminderTimeFormatted
            val date = x.reminderDateFormatted
            val title = x.title
            println(currentTime)
            println(time)
            if (time == currentTime && date == currentDate){
                println("IT DOES")
                // toast todo change text to display actual notification
                handler.post{
                    val text = "Reminder: ${title}"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration) // in Activity
                    toast.show()

                    Handler().postDelayed({},1000)
                }

            }
        }

    }


    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentDate(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(calendar.time)
    }
}