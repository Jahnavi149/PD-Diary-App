package com.example.pddiary.utils

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Logger {

    private const val LOG_FILE_NAME = "app_log.csv"
    private const val LOG_DIRECTORY_NAME = "app_logs"  // Directory where the log file will be saved

    @Synchronized
    fun logEvent(context: Context, event: String) {
        val logDir = File(context.filesDir, LOG_DIRECTORY_NAME)
        if (!logDir.exists()) {
            logDir.mkdirs()  // Create the directory if it doesn't exist
        }

        val file = File(logDir, LOG_FILE_NAME)
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val logEntry = "$timestamp, $event\n"

        try {
            FileWriter(file, true).use { writer ->
                writer.append(logEntry)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun logPageDuration(context: Context, pageName: String, startTime: Long) {
        val duration = System.currentTimeMillis() - startTime
        logEvent(context, "Spent $duration ms on $pageName")
    }
}
