package ru.niisokb.mcc.framework.dpm

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import ru.niisokb.mcc.framework.admin.AdminReceiver

class DpmServices {

    fun getDpm(context: Context): DevicePolicyManager {
        return context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    fun getAdmin(context: Context): ComponentName {
        return ComponentName(context, AdminReceiver::class.java)
    }
}
