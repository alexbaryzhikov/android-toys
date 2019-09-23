package com.alexb.clearappdata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    private val tag = "CleanUpLog"
    private val gmailUid = "com.google.android.gm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        scope.launch {
            runCatching { cleanUpData2() }.onFailure {
                Log.e(tag, "Error: $it")
            }
        }
    }

    private fun cleanUpData() {
        val cache = cacheDir?.parentFile?.parentFile
        require(cache != null) { "cache == null" }
        val appDir = File(cache, gmailUid)
        if (appDir.exists()) {
            Log.d(tag, "Successfully found: $appDir")
            val children = appDir.list()
            children?.forEach { Log.d(tag, it) }
        } else {
            Log.e(tag, "File not found: $appDir")
        }
    }

    private fun cleanUpData2() {
        val c = Class.forName("android.app.ActivityManager")
        val m = c.getMethod(
            "clearApplicationUserData",
            String::class.java/*, IPackageDataObserver::class.java */
        )
        m.invoke(c, gmailUid, null)
    }
}
