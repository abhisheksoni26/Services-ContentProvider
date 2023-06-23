package com.example.services.reciever


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.services.MainActivity
import com.example.services.R
import com.example.services.interfaces.MyInterface


class MyBroadcast(private val listener: MyInterface) : BroadcastReceiver() {
    val TAG = "Reciever"
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            "com.example.mynotes.ACTION_SEND" -> {
                val data = intent.getStringExtra("com.example.mynotes.EXTRA_DATA")
                Log.e(TAG, "onReceive: ${data.toString()}")
                listener.sendData(data.toString())

                sendNotification(context, data)
            }
        }
    }

    private fun sendNotification(context: Context, data: String?) {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("key", data.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "notification_id"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Received Data")
            .setContentText(data.toString())
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since Android Oreo, notification channels are required.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "notification_id",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())

    }
}