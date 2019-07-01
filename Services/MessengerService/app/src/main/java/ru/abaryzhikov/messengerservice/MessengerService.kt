package ru.abaryzhikov.messengerservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MessengerService : Service() {

    private lateinit var messenger: Messenger

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate() called")
        messenger = Messenger(MessageHandler(this))
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG, "onBind() called")
        return messenger.binder
    }

    class MessageHandler(private val context: Context) : Handler() {

        private val timeFormat = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
        private val now: String
            get() = timeFormat.format(Date(System.currentTimeMillis()))

        override fun handleMessage(msg: Message?) {
            Log.v(TAG, "handleMessage() called")
            when (msg?.what) {
                MSG_GET_TIMESTAMP -> {
                    val t = now
                    Toast.makeText(context, "[service] $t", Toast.LENGTH_SHORT).show()
                    val payload = Bundle().apply { putString(TIMESTAMP_KEY, t) }
                    val reply = Message.obtain(null, MSG_GET_TIMESTAMP, payload)
                    msg.replyTo.send(reply)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MessengerService"
        private const val MSG_GET_TIMESTAMP = 1
        private const val TIMESTAMP_KEY = "Timestamp"
    }
}