package com.alexbaryzhikov.kotlinplayground

import android.os.Bundle
import android.util.Log
import com.alexbaryzhikov.kotlinplayground.coroutines.AuthUser
import com.alexbaryzhikov.kotlinplayground.coroutines.ScopedAppActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor

class MainActivity : ScopedAppActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun logException() {
        try {
            val a = 10/0
        } catch (th: Throwable) {
            Log.w(TAG, "[Message]", th)
            Log.w(TAG, "[Message]\n$th\n${th.stackTrace.fold("") {acc, element -> "$acc    at $element\n" } }")
        }
    }

    private fun fireCoroutine() {
        launch {
            Log.i(TAG, "CoroutineName: ${coroutineContext[CoroutineName]}")
            Log.i(TAG, "ContinuationInterceptor: ${coroutineContext[ContinuationInterceptor]}")
            Log.i(TAG, "Job: ${coroutineContext[Job]}")
            Log.i(TAG, "CoroutineExceptionHandler: ${coroutineContext[CoroutineExceptionHandler]}")
            Log.i(TAG, "AuthUser: ${coroutineContext[AuthUser]}")
            delay(2000)
            textView.text = getString(R.string.coroutine_text)
        }
        textView.text = getString(R.string.sequential_text)
    }
}
