package ru.abaryzhikov.messengerservice

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import ru.abaryzhikov.messengerservice.App.Companion.CHANNEL_ID

class MessengerService : Service() {

    private lateinit var messenger: Messenger

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Messenger Service")
            .setSmallIcon(R.drawable.ic_messenger)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG, "onBind() called")
        messenger = Messenger(MessageHandler(this))
        return messenger.binder
    }

    class MessageHandler(private val context: Context) : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_GET_TIMESTAMP ->
                    Toast.makeText(context, "t = System.currentTimeMillis()", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "MessengerService"
        private const val MSG_GET_TIMESTAMP = 1
    }
}