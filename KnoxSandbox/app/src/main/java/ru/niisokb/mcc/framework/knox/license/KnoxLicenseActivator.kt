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
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_activation),
                context.getString(R.string.license_progress)
            )
            showToast(context, message)
        }.onFailure {
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_activation),
                context.getString(R.string.license_failed)
            )
            Log.e(TAG, "$message: $it")
            showToast(context, message)
        }
    }

    fun deactivateLicense(context: Context, kpe: String) {
        val klm = KnoxEnterpriseLicenseManager.getInstance(context)
        runCatching {
            klm.deActivateLicense(kpe)
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_deactivation),
                context.getString(R.string.license_progress)
            )
            showToast(context, message)
        }.onFailure {
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_deactivation),
                context.getString(R.string.license_failed)
            )
            Log.e(TAG, "$message: $it")
            showToast(context, message)
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
