package com.example.studenttodo

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar

class NotifsBackground : Service() {

    private lateinit var handler: Handler

    private val runnable = object : Runnable {
        override fun run() {
            checkTimeAndPerformAction()
            handler.postDelayed(this, INTERVAL) // Run the code at a specified interval
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
        handler.postDelayed(runnable, INTERVAL)
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
        // Put your time comparison and action code here
        val storedTime = "12:30"
        val currentTime = getCurrentTime()

        val text = "Time is $currentTime Date is ${getCurrentDate()}"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration) // in Activity
        toast.show()
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentDate(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("DD-MM-YYYY")
        return dateFormat.format(calendar.time)
    }
}