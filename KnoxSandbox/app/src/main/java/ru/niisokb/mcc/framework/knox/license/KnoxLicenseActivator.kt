package ru.niisokb.mcc.framework.knox.license

import android.content.Context
import android.util.Log
import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class KnoxLicenseActivator {

    fun activateLicense(context: Context, kpe: String) {
        val klm = KnoxEnterpriseLicenseManager.getInstance(context)
        runCatching {
            klm.activateLicense(kpe)
            Log.d(TAG, context.getString(R.string.license_progress))
            showToast(context, R.string.license_progress)
        }.onFailure {
            Log.e(TAG, "${context.getString(R.string.license_failed)}: $it")
            showToast(context, context.getString(R.string.license_failed))
        }
    }

    fun isKpeApiAccessible(context: Context): Boolean {
        val edm = EnterpriseDeviceManager.getInstance(context)
        return runCatching {
            edm?.applicationPolicy?.getApplicationName(context.packageName)
        }.isSuccess
    }

    companion object {
        private const val TAG = "KnoxLicenseActivator"
    }
}
