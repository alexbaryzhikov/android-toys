package ru.niisokb.mcc.framework.knox.license

import android.content.Context
import android.util.Log
import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.interfaces.SharedPreferencesDataSource
import ru.niisokb.mcc.framework.utils.showToast

class KnoxLicenseActivator(private val sharedPreferencesDataSource: SharedPreferencesDataSource) {

    fun activateLicense(context: Context, kpe: String) {
        Log.d(TAG, "Activate $kpe")
        val klm = KnoxEnterpriseLicenseManager.getInstance(context)
        runCatching {
            klm.activateLicense(kpe)
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_activation),
                context.getString(R.string.license_progress)
            )
            showToast(context, message)
            Log.d(TAG, message)
        }.onFailure {
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_activation),
                context.getString(R.string.license_failed)
            )
            showToast(context, message)
            Log.e(TAG, "$message: $it")
        }.onSuccess {
            sharedPreferencesDataSource.save(context, LAST_KPE_KEY, kpe)
        }
    }

    fun deactivateLicense(context: Context, kpe: String? = null) {
        val kpeToDeactivate = kpe ?: sharedPreferencesDataSource.load(context, LAST_KPE_KEY)
        if (kpeToDeactivate == null) {
            Log.w(TAG, "No license provided for deactivation")
            return
        }
        Log.d(TAG, "Deactivate $kpeToDeactivate")
        val klm = KnoxEnterpriseLicenseManager.getInstance(context)
        runCatching {
            klm.deActivateLicense(kpeToDeactivate)
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_deactivation),
                context.getString(R.string.license_progress)
            )
            showToast(context, message)
            Log.d(TAG, message)
        }.onFailure {
            val message = context.getString(
                R.string.license_status,
                context.getString(R.string.license_deactivation),
                context.getString(R.string.license_failed)
            )
            showToast(context, message)
            Log.e(TAG, "$message: $it")
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
        private const val LAST_KPE_KEY = "last_kpe_key"
    }
}
