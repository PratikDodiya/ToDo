package com.pd.todo.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.pd.todo.R


class NotificationUtils(base: Context, title: String, description: String, time: String) : ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null
    private val context: Context
    val channelId = base.getString(R.string.notification_channel_id)
    private val title: String = title
    private val description: String = description
    private val time: String = time

    @SuppressLint("RemoteViewLayout")
    fun setNotification(title: String?, body: String?, time: String?): NotificationCompat.Builder {

        val contentView = RemoteViews(packageName, R.layout.layout_notification)
        contentView.setTextViewText(R.id.tvTitle, title)
        contentView.setTextViewText(R.id.tvDescription, body)
        contentView.setTextViewText(R.id.tvDateTime, time)

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
//            .setContent(contentView)
            .setContentTitle("$title  ($time)")
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, baseContext.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager!!.createNotificationChannel(channel)
        }
    }

    val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    fun setReminder(timeInMillis: Long, interval: Int) {
        val intent = Intent(context, ToDoBroadcast::class.java)
        intent.putExtra("TITLE", title)
        intent.putExtra("DESCRIPTION", description)
        intent.putExtra("TIME", time)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY * interval, pendingIntent)
    }

    init {
        context = base
        createChannel()
    }
}