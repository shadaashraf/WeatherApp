package com.example.forcastingapp.alert.receiver

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.forcastingapp.R

class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmType = intent.getIntExtra("ALARM_TYPE", 0)

        when (alarmType) {
            1 -> showNotification(context) // Custom notification
            2 -> playDefaultAlarmSound(context) // Default alarm sound
            else -> showNotification(context) // Default to notification
        }
    }

    private fun showNotification(context: Context) {
        val notificationId = 1
        val notification = NotificationCompat.Builder(context, "weather_alert_channel")
            .setSmallIcon(R.drawable.sunny_icon)
            .setContentTitle("Weather Alert")
            .setContentText("Your weather alert is active.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
            return // Exit the function to wait for the user's response
        }

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    private fun playDefaultAlarmSound(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(500) // Vibrate for 500ms

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()
    }

    companion object {
        const val REQUEST_NOTIFICATION_PERMISSION = 1001 // Define the request code for permission
    }
}
