package ru.abaryzhikov.happylibrary

import android.content.Context
import android.widget.Toast

class HelloWorld {

    fun sayHello(context: Context) {
        Toast.makeText(context, "Hello world!", Toast.LENGTH_LONG).show()
    }
}