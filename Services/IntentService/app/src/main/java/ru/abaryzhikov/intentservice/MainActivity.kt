package ru.abaryzhikov.intentservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendButton.performClick()
                true
            } else {
                false
            }
        }

        sendButton.setOnClickListener {
            val message = inputView.text.toString()
            val intent = Intent(this@MainActivity, MyIntentService::class.java)
            intent.putExtra(MyIntentService.MESSAGE_KEY, message)
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        setupReceiver()
    }

    private fun setupReceiver() {
        receiver = ResultReceiver()
        val intentFilter = IntentFilter().apply { addAction(FILTER_ACTION_KEY) }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    inner class ResultReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra(MyIntentService.RESULT_KEY)
            val s = "${outputView.text}\n$message"
            outputView.text = s
        }
    }

    companion object {
        const val FILTER_ACTION_KEY = "42"
    }
}
