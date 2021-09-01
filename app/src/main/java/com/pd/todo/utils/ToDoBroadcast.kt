package com.pd.todo.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ToDoBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationUtils = NotificationUtils(
            context!!,
            intent!!.getStringExtra("TITLE"),
            intent!!.getStringExtra("DESCRIPTION"),
            intent!!.getStringExtra("TIME")
        )
        val builder = notificationUtils.setNotification(intent!!.getStringExtra("TITLE"), intent!!.getStringExtra("DESCRIPTION"), intent!!.getStringExtra("TIME"))
        notificationUtils.manager!!.notify(101, builder.build())
    }
}