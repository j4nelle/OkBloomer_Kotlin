package es.uc3m.android.okbloomer_kotlin

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

@SuppressLint("ScheduleExactAlarm")
fun scheduleWateringNotification(context: Context, plantId: Int, nickname: String, frequencyInDays: Float) {
    val intent = Intent(context, WaterReminderReceiver::class.java).apply {
        putExtra("nickname", nickname)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        plantId, // usamos plantId para distinguir notificaciones
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    //val triggerTime = System.currentTimeMillis() + (frequencyInDays * 24 * 60 * 60 * 1000).toLong()
    //Ying- test with less time
    val triggerTime = System.currentTimeMillis() + (frequencyInDays * 1000).toLong()


    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        pendingIntent
    )

    Log.d("NOTIF", "Scheduled notification for $nickname in $frequencyInDays days (triggerTime=$triggerTime)")
    Log.d("NOTIF_DEBUG", "Notification fired for plant: $nickname at ${System.currentTimeMillis()}")
}