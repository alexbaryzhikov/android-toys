package ru.niisokb.mcc.framework.knox.actions

import android.content.Context
import android.util.Log
import com.samsung.android.knox.EnterpriseDeviceManager
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class WipeAppDataAction {

    fun wipeAppData(context: Context, packageName: String) {
        val edm = EnterpriseDeviceManager.getInstance(context)
        val ap = edm.applicationPolicy
        runCatching { 
            val result = ap.wipeApplicationData(packageName)
            if (result) {
                val msg = context.getString(R.string.data_wipe_success, packageName)
                Log.d(TAG, msg)
                showToast(context, msg)
            } else {
                val msg = context.getString(R.string.data_wipe_failure, packageName)
                Log.d(TAG, msg)
                showToast(context, msg)
            }
        }.onFailure {
            val msg = context.getString(R.string.data_wipe_failure, packageName)
            Log.d(TAG, "$msg: $it")
            showToast(context, msg)
        }
    }
    
    companion object {
        private const val TAG = "WipeAppDataAction" 
    }
}