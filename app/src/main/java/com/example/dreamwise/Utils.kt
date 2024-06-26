package com.example.dreamwise
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class Utils {
    companion object {
        const val NOTIFICATION_ID = 22
        const val NOTIFICATION_CHANNEL_ID = "notify"

        fun sendNotification(context: Context, msg: String?) {
            val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Prepare intent which is triggered if the notification is selected
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("notificationID", Utils.NOTIFICATION_ID)
            intent.putExtra("msg", msg)

            // A PendingIntent is a token that you give to another application (e.g. Notification Manager,
            // Alarm Manager or other 3rd party applications), which allows this other application to use
            // the permissions of your application to execute a predefined piece of code.
            val pIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "My Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                // Configure the notification channel.
                notificationChannel.description = "Channel description"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("TextOnStatus") // .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Notification Title")
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(longArrayOf(1000, 1000, 0, 0, 0))
                .setContentText(msg)
                .setContentInfo("Notification Detail")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            Log.d("NOTIFICATION", "sendNotification end")
        }
    }

}