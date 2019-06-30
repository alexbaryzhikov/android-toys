package ru.abaryzhikov.intentservice

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.text.SimpleDateFormat
import java.util.*

class MyIntentService : IntentService("MyIntentService") {

    private val dateFormat = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val message = intent.getStringExtra(MESSAGE_KEY)
        SystemClock.sleep(2000)
        val time = dateFormat.format(Date(System.currentTimeMillis()))
        val result = "[${Thread.currentThread().name}] $time $message"
        intent.apply {
            action = MainActivity.FILTER_ACTION_KEY
            putExtra(RESULT_KEY, result)
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Intent service CREATED", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Intent service DESTROYED", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val MESSAGE_KEY = "message"
        const val RESULT_KEY = "result"
    }
}