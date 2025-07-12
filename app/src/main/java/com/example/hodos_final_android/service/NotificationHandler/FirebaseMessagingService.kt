package com.example.hodos_final_android.service.NotificationHandler

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hodos_final_android.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Received message from: ${remoteMessage.from}")

        // Log notification payload if exists
        remoteMessage.notification?.let {
            Log.d("FCM", "Notification received - Title: ${it.title}, Body: ${it.body}")
            showNotification(it.title, it.body)
        }

        // Log data payload if exists
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Data payload: ${remoteMessage.data}")
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String?, message: String?) {
        Log.d("FCM", "Showing notification - Title: $title, Body: $message")

        val builder = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title ?: "Notifications")
            .setContentText(message ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val channelName = "General"
            val existingChannel = notificationManager.getNotificationChannel(channelId)
            if (existingChannel == null) {
                Log.d("FCM", "Creating notification channel: $channelName")
                val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
        }

        notificationManager.notify(1, builder.build())
    }
}
