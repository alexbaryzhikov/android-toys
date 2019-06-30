package ru.abaryzhikov.messengerservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, MessengerService::class.java))
        closeButton.setOnClickListener { this@MainActivity.finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MessengerService::class.java))
    }
}
