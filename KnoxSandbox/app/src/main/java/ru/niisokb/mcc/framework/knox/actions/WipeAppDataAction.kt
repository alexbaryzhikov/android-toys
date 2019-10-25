package ru.niisokb.mcc.framework.knox.actions

import android.content.Context
import android.util.Log
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.knox.KnoxServices
import ru.niisokb.mcc.framework.utils.showToast

class WipeAppDataAction(private val knoxServices: KnoxServices) {

    fun wipeAppData(context: Context, packageName: String) {
        val applicationPolicy = knoxServices.getEdm(context).applicationPolicy
        runCatching { 
            val result = applicationPolicy.wipeApplicationData(packageName)
            if (result) {
                val msg = context.getString(R.string.data_wipe_success, packageName)
                showToast(context, msg)
            } else {
                val msg = context.getString(R.string.data_wipe_failure, packageName)
                showToast(context, msg)
            }
        }.onFailure {
            val msg = context.getString(R.string.data_wipe_failure, packageName)
            Log.e(TAG, "$msg: $it")
            showToast(context, msg)
        }
    }
    
    companion object {
        private const val TAG = "WipeAppDataAction" 
    }
}