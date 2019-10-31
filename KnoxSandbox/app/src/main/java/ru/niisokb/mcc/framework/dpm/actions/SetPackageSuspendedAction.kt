package ru.niisokb.mcc.framework.dpm.actions

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ru.niisokb.mcc.framework.dpm.DpmServices

@RequiresApi(Build.VERSION_CODES.N)
class SetPackageSuspendedAction(private val dpmServices: DpmServices) {

    fun suspendApp(context: Context) {
        val dpm = dpmServices.getDpm(context)
        val admin = dpmServices.getAdmin(context)

        val result = runCatching {
            val suspended = dpm.isPackageSuspended(admin, PACKAGE_TO_SUSPEND)
            dpm.setPackagesSuspended(admin, arrayOf(PACKAGE_TO_SUSPEND), !suspended)
        }.onFailure {
            Log.e(TAG, "Failed suspend $PACKAGE_TO_SUSPEND: $it")
        }.getOrNull()

        if (result != null && PACKAGE_TO_SUSPEND !in result) {
            Log.d(TAG, "$PACKAGE_TO_SUSPEND was successfully suspended/unsuspended")
        } else {
            Log.e(TAG, "$PACKAGE_TO_SUSPEND was not suspended/unsuspended")
        }
    }

    companion object {
        private const val TAG = "SetPackageSuspended"
        private const val PACKAGE_TO_SUSPEND = "com.google.android.youtube"
    }
}