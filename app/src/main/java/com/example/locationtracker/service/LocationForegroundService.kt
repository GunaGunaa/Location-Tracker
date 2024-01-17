package com.example.locationtracker.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.locationtracker.R
import com.example.locationtracker.activity.LTHomeActivity
import java.util.concurrent.TimeUnit

class LocationForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        // Perform any setup if needed
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create a notification and make this service run in the foreground
        val notification = createNotification()

        initWorkManager()
        startForeground(FOREGROUND_SERVICE_ID, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Perform cleanup if needed
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, LTHomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Updates")
            .setContentText("Running in the background")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "LocationTrackerChannel"
        const val FOREGROUND_SERVICE_ID = 123
    }

    /**
     * to initialize the work manager for getting location 15 min once
     */
    private fun initWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest = PeriodicWorkRequestBuilder<LocationUpdateWorker>(
            repeatInterval = 15, // in minutes
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(
            periodicWorkRequest
        )
    }
}