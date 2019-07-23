package ru.abaryzhikov.appmodules

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.abaryzhikov.happylibrary.HelloWorld

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = packageName

        textView.setOnClickListener {
            HelloWorld().sayHello(this)
        }
    }
}
