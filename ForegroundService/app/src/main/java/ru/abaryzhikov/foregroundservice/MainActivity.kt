package ru.abaryzhikov.foregroundservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton.setOnClickListener {
            startService(Intent(it.context, ForegroundService::class.java))
        }
        stopButton.setOnClickListener {
            stopService(Intent(it.context, ForegroundService::class.java))
        }
    }
}
