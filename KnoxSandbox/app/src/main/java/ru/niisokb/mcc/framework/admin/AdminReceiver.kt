package ru.niisokb.mcc.framework.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class AdminReceiver : DeviceAdminReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(TAG, "action = ${intent.action}")
    }

    override fun onEnabled(context: Context, intent: Intent) {
        Log.d(TAG, context.getString(R.string.admin_enabled))
        showToast(context, R.string.admin_enabled)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Log.d(TAG, context.getString(R.string.admin_disabled))
        showToast(context, R.string.admin_disabled)
    }

    companion object {
        private const val TAG = "AdminReceiver"
    }
}