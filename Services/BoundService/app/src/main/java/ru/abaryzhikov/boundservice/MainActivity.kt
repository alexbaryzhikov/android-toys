package ru.abaryzhikov.boundservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("ConstantConditionIf")
class MainActivity : AppCompatActivity(), ServiceConnection {

    private var service: BoundService? = null
    private val bound: Boolean
        get() = service != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateTimestampButton.setOnClickListener {
            if (bound) {
                timestampView.text = service!!.timestamp
            }
        }

        bindButton.setOnClickListener {
            val intent = Intent(applicationContext, BoundService::class.java)
            if (EXPLICIT_START) {
                startService(intent)
            }
            bindService(intent, this, Context.BIND_AUTO_CREATE)
        }

        stopButton.setOnClickListener {
            if (bound) {
                unbindService(this)
                service = null
            }
            if (EXPLICIT_START) {
                val intent = Intent(applicationContext, BoundService::class.java)
                stopService(intent)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(this)
            service = null
        }
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        Log.v(TAG, "onServiceConnected() called")
        val serviceBinder = binder as? BoundService.ServiceBinder
        service = serviceBinder?.service
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.v(TAG, "onServiceDisconnected() called")
        service = null
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val EXPLICIT_START = false
    }
}
