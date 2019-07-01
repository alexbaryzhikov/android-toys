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

    private var serviceMessenger: Messenger? = null
    private var bound: Boolean = false
    private val connection = MessengerServiceConnection()
    private val incomingMessenger = Messenger(IncomingHandler { callbackView.text = it })

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
            callbackView.text = getString(R.string.disconnected)
        }
    }

    private fun askTimestamp() {
        if (!bound) return
        val msg: Message = Message.obtain(null, MSG_GET_TIMESTAMP, 0, 0)
        msg.replyTo = incomingMessenger
        try {
            serviceMessenger?.send(msg)
        } catch (e: RemoteException) {
            Log.e(TAG, "Can't send message", e)
        }
    }

    class IncomingHandler(private val receiver: (String?) -> Unit) : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_GET_TIMESTAMP -> receiver((msg.obj as Bundle).getString(TIMESTAMP_KEY))
                else -> super.handleMessage(msg)
            }
        }
    }

    inner class MessengerServiceConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceMessenger = Messenger(service)
            bound = true
            callbackView.text = getString(R.string.connected)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceMessenger = null
            bound = false
            callbackView.text = getString(R.string.disconnected)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val MSG_GET_TIMESTAMP = 1
        private const val TIMESTAMP_KEY = "Timestamp"
    }
}
