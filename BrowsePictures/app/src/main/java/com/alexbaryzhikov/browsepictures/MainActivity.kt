package com.alexbaryzhikov.browsepictures

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val READ_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener { performFileSearch() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri: Uri
            if (data != null) {
                uri = data.data!!
                Log.i(javaClass.simpleName, "Uri: $uri")
                showImage(uri)
            }
        }
    }

    private fun performFileSearch() {
        intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    private fun showImage(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val drawable = Drawable.createFromStream(inputStream, uri.toString())
        findViewById<ImageView>(R.id.image).setImageDrawable(drawable)
    }
}
