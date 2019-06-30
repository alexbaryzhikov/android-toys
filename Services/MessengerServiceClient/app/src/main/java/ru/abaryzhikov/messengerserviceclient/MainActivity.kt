package ru.abaryzhikov.messengerserviceclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var messenger: Messenger? = null
    private var bound: Boolean = false
    private val connection = MessengerServiceConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askButton.setOnClickListener { askTimestamp() }
        closeButton.setOnClickListener { this@MainActivity.finish() }
        bindButton.setOnClickListener { doBind() }
    }

    private fun doBind() {
        val intent = Intent().apply {
            setClassName(
                "ru.abaryzhikov.messengerservice",
                "ru.abaryzhikov.messengerservice.MessengerService"
            )
        }
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service.
        if (bound) {
            unbindService(connection)
            bound = false
            callbackView.text = "Disconnected"
        }
    }

    private fun askTimestamp() {
        if (!bound) return
        val msg: Message = Message.obtain(null, MSG_GET_TIMESTAMP, 0, 0)
        try {
            messenger?.send(msg)
        } catch (e: RemoteException) {
            Log.e(TAG, "Can't send message", e)
        }
    }

    inner class MessengerServiceConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
            bound = true
            callbackView.text = "Connected"
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            messenger = null
            bound = false
            callbackView.text = "Disconnected"
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val MSG_GET_TIMESTAMP = 1
        private const val BIND_SERVICE_PERMISSION = "ru.abaryzhikov.messengerservice.BIND_SERVICE"
    }
}
