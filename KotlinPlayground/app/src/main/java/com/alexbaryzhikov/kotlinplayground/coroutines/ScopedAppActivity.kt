package com.alexbaryzhikov.kotlinplayground.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedAppActivity : AppCompatActivity(), CoroutineScope {
    protected lateinit var job: Job
    protected lateinit var authUser: AuthUser

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + authUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        authUser = AuthUser("John Doe")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}