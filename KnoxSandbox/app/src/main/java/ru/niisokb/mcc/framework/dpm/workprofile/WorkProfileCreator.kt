package ru.niisokb.mcc.framework.dpm.workprofile

import android.app.Activity
import android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME
import android.content.Intent
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class WorkProfileCreator {

    fun createWorkProfile(provisioningActivity: Activity) {
        val context = provisioningActivity.applicationContext
        val intent = Intent(PROVISIONING_ACTION)
            .putExtra(EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME, context.packageName)
        if (intent.resolveActivity(context.packageManager) != null) {
            provisioningActivity.startActivityForResult(intent, PROVISIONING_REQUEST_CODE)
            provisioningActivity.finish()
        } else {
            showToast(context, R.string.profile_provisioning_no_handler)
        }
    }

    companion object {
        private const val PROVISIONING_ACTION = "android.app.action.PROVISION_MANAGED_PROFILE"
        private const val PROVISIONING_REQUEST_CODE = 11
    }
}