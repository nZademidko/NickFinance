package com.nickfinance.nikfinance.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsMessage
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nickfinance.nikfinance.R


class MessageReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationData = intent!!.extras
        if (notificationData != null) {
            val title = notificationData.getString("title")
            val body = notificationData.getString("body")

            var builder = NotificationCompat.Builder(context!!, "1")
                .setSmallIcon(R.drawable.ic_coins)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager = NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManager.notify(1, builder.build())
        }
    }
}