package ru.niisokb.mcc.framework.knox.container

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.samsung.android.knox.container.KnoxContainerManager.*
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast


class ContainerReceiver : BroadcastReceiver() {

    var initialRequestId: Int = -1

    override fun onReceive(context: Context?, intent: Intent?) {
        require(context != null) { "context is null" }
        val extras = intent?.extras
        require(extras != null) { "extras is null" }

        val containerCreationRequestId = extras.getInt(CONTAINER_CREATION_REQUEST_ID, -1)
        val statusCode = extras.getInt(CONTAINER_CREATION_STATUS_CODE, ERROR_INTERNAL_ERROR)

        if (initialRequestId == containerCreationRequestId) {
            Log.d(TAG, "Intent belongs to another MDM or a fake Intent.")
            return
        }

        if (statusCode >= 0) {
            showToast(context, context.getString(R.string.container_creation_success))
        } else {
            showToast(context, context.getString(R.string.container_creation_error, statusCode))
        }

        unregister(context)
    }

    fun register(context: Context) {
        val filter = IntentFilter(ACTION_CONTAINER_CREATION_STATUS)
        runCatching {
            context.registerReceiver(this, filter)
        }.onFailure {
            Log.e(TAG, "Failed to register receiver: $it")
        }.onSuccess {
            Log.d(TAG, "Receiver registered")
        }
    }

    fun unregister(context: Context) {
        runCatching {
            context.unregisterReceiver(this)
        }.onFailure {
            Log.d(TAG, "Failed to unregister receiver: $it")
        }.onSuccess {
            Log.d(TAG, "Receiver unregistered")
        }
    }

    companion object {
        private const val TAG = "ContainerReceiver"
    }
}