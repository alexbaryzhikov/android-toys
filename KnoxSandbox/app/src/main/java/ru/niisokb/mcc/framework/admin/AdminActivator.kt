package ru.niisokb.mcc.framework.admin

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.DEVICE_POLICY_SERVICE
import android.content.Intent
import android.util.Log
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class AdminActivator {

    fun activateAdmin(context: Context) {
        val admin = getAdmin(context)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Admin privileges required")
        context.startActivity(intent)
    }

    fun deactivateAdmin(context: Context) {
        runCatching {
            getDpm(context).removeActiveAdmin(getAdmin(context))
        }.onFailure {
            Log.e(TAG, "${context.getString(R.string.admin_disable_failed)}: $it")
            showToast(context, R.string.admin_disable_failed)
        }
    }

    fun isAdminActive(context: Context): Boolean {
        return getDpm(context).isAdminActive(getAdmin(context))
    }

    companion object {

        private const val TAG = "AdminActivator"

        fun getDpm(context: Context): DevicePolicyManager {
            return context.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        }

        fun getAdmin(context: Context): ComponentName {
            return ComponentName(context, AdminReceiver::class.java)
        }
    }
}
