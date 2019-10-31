package ru.niisokb.mcc.framework.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.dpm.DpmServices
import ru.niisokb.mcc.framework.utils.showToast

class AdminReceiver(private val dpmServices: DpmServices) : DeviceAdminReceiver() {

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

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        Log.d(TAG, context.getString(R.string.profile_provisioning_complete))
        showToast(context, R.string.profile_provisioning_complete)
        val dpm = dpmServices.getDpm(context)
        val admin = dpmServices.getAdmin(context)
        dpm.setProfileName(admin, WORK_PROFILE_NAME)
        dpm.setProfileEnabled(admin)
        dpm.enableSystemApp(admin, context.getString(R.string.camera_app_uid))
    }

    companion object {
        private const val TAG = "AdminReceiver"
        private const val WORK_PROFILE_NAME = "My Work Profile"
    }
}