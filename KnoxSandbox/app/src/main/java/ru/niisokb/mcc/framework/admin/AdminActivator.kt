package ru.niisokb.mcc.framework.admin

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.dpm.DpmServices
import ru.niisokb.mcc.framework.utils.showToast

class AdminActivator(private val dpmServices: DpmServices) {

    fun activateAdmin(context: Context) {
        val admin = dpmServices.getAdmin(context)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Admin privileges required")
        context.startActivity(intent)
    }

    fun deactivateAdmin(context: Context) {
        runCatching {
            dpmServices.getDpm(context).removeActiveAdmin(dpmServices.getAdmin(context))
        }.onFailure {
            Log.e(TAG, "${context.getString(R.string.admin_disable_failed)}: $it")
            showToast(context, R.string.admin_disable_failed)
        }
    }

    fun isAdminActive(context: Context): Boolean {
        return dpmServices.getDpm(context).isAdminActive(dpmServices.getAdmin(context))
    }

    fun isOwnerActive(context: Context): Boolean {
        val dpm = dpmServices.getDpm(context)
        return dpm.isDeviceOwnerApp(context.packageName) ||
                dpm.isProfileOwnerApp(context.packageName)
    }

    companion object {
        private const val TAG = "AdminActivator"
    }
}
