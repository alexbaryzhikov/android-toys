package com.alexbaryzhikov.dirlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.stericson.roottools.RootTools
import com.stericson.roottools.execution.Command

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val TAG = "MainActivity"
    }

    private lateinit var button: Button
    private lateinit var fileList: RecyclerView
    private lateinit var currentPath: String
    private lateinit var filesViewAdapter: FilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!havePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        button = findViewById(R.id.home_button)
        fileList = findViewById(R.id.file_list)

        val internalStorage = Environment.getExternalStorageDirectory().absolutePath.trimEnd('/')
        button.setOnClickListener {
            fetchItems(internalStorage)
        }

        val filesViewManager = LinearLayoutManager(this)
        filesViewAdapter = FilesAdapter(this, this::fetchItems)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        fileList.apply {
            layoutManager = filesViewManager
            adapter = filesViewAdapter
            addItemDecoration(divider)
        }

        fetchItems(internalStorage)
    }

    private fun havePermission(perm: String) =
            ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission(perm: String) =
            ActivityCompat.requestPermissions(this, arrayOf(perm), PERMISSION_REQUEST_CODE)

    private fun fetchItems(path: String) {
        val start = System.currentTimeMillis()
        currentPath = path
        val fileDirItems = mutableListOf<FileDirItem>()
        val cmd = "ls -Aog $path"
        val command = object : Command(0, cmd) {
            override fun commandOutput(id: Int, line: String?) {
                if (line != null && !line.startsWith("total") && !line.startsWith("ls:")) {
                    fileDirItems += line.toFileDirItem()
                }
                super.commandOutput(id, line)
            }

            override fun commandCompleted(id: Int, exitcode: Int) {
                receiveItems(fileDirItems)
                Log.d(TAG, "Fetch completed in ${(System.currentTimeMillis() - start) / 1000.0}s")
                super.commandCompleted(id, exitcode)
            }
        }
        runCommand(command)
    }

    private fun receiveItems(fileDirItems: List<FileDirItem>) {
        filesViewAdapter.fileDirItems = fileDirItems
    }

    private fun String.toFileDirItem(): FileDirItem {
        // Example input:
        // -rw-rw---- 1  538977 2017-11-30 00:37 neck.jpg

        // First 9 symbols are permissions
        val perms = this.substring(0, 10)

        // Search start/end of the size field
        var pos = 11
        while (this[++pos] == ' ');
        var start = pos
        while (this[++pos] != ' ');
        val fileSize = this.substring(start, pos).toLong()

        // Next 16 symbols are the last modified date-time
        start = pos + 1
        pos = start + 16
        val lastModified = this.substring(start, pos)

        // The rest is file/dir name
        var fileName = this.substring(pos + 1)
        if (fileName.contains(" -> ")) {
           fileName = fileName.substringBefore(" -> ")
        }

        return FileDirItem("$currentPath/$fileName", perms.startsWith('d'), fileSize, lastModified)
    }

    private fun runCommand(command: Command) {
        try {
            RootTools.getShell(false).add(command)
        } catch (e: Exception) {
            showToast("Shell error: $e")
        }
    }

    private fun showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
        if (isOnMainThread()) {
            Toast.makeText(this, msg, length).show()
        } else {
            runOnUiThread {
                Toast.makeText(this, msg, length).show()
            }
        }
    }

    private fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()
}
