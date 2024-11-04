package com.example.forcastingapp.alert.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.forcastingapp.alert.receiver.AlertReceiver

class AlarmScheduler(private val context: Context) {

    fun scheduleAlarm(duration: Long, alarmType: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java).apply {
            putExtra("ALARM_TYPE", alarmType)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerTime = System.currentTimeMillis() + duration
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    fun cancelAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent) // Cancel the alarm
    }
}
