package ru.abaryzhikov.boundservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer

class BoundService : Service() {

    private val binder = ServiceBinder()
    private lateinit var chronometer: Chronometer

    val timestamp: String
        get() {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val hours = elapsedMillis / 3_600_000
            val minutes = (elapsedMillis - hours * 3_600_000) / 60000
            val seconds = (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000
            val millis = elapsedMillis - (hours * 3600000) - (minutes * 60000) - (seconds * 1000)
            return "$hours:$minutes:$seconds:$millis"
        }

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate() called")
        chronometer = Chronometer(this).apply {
            base = SystemClock.elapsedRealtime()
            start()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG, "onBind() called")
        return binder
    }

    override fun onRebind(intent: Intent?) {
        Log.v(TAG, "onRebind() called")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(TAG, "onUnbind() called")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy() called")
    }

    inner class ServiceBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }

    companion object {
        private const val TAG = "BoundService"
    }
}