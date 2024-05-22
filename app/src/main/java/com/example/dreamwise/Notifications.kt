package com.example.dreamwise

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class Notifications(private val context: Context) {

    private val tagforLogcat = "NotificationHelper"

    fun showNotification(title: String, message: String) {
        val channelId = "channel_id"
        val channelName = "Channel Name"

        // Create the notification channel (required for API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel Description"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Check for notification permission (required for API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((context as MainActivity), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                return
            }
        }

        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Get the sound URI
        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/raw/notification_sound")

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.dreamwiselogo) // Ensure this resource exists
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(soundUri)

        with(NotificationManagerCompat.from(context)) {
            // Log when notification is sent
            Log.d(tagforLogcat, "Sending notification")
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }
}
