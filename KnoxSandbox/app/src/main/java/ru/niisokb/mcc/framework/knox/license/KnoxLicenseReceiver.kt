package ru.niisokb.mcc.framework.knox.license

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager.*
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class KnoxLicenseReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val msgRes: Int
        if (intent == null) {
            // No intent action is available
            showToast(context, R.string.no_intent)
            return
        } else {
            val action = intent.action
            if (action == null) {
                // No intent action is available
                showToast(context, R.string.no_intent_action)
                return
            } else if (action == ACTION_LICENSE_STATUS) {
                intent.showLicenseActivationInfo()
                // Intent from KPE license activation attempt is obtained
                val errorCode = intent.getIntExtra(
                    EXTRA_LICENSE_ERROR_CODE,
                    DEFAULT_ERROR_CODE
                )

                if (errorCode == ERROR_NONE) {
                    // license activated successfully
                    showToast(context, R.string.klm_activated_successfully)
                    Log.d(TAG, context.getString(R.string.klm_activated_successfully))
                    return
                } else {
                    // license activation failed
                    when (errorCode) {
                        ERROR_INTERNAL -> msgRes =
                            R.string.err_klm_internal
                        ERROR_INTERNAL_SERVER -> msgRes =
                            R.string.err_klm_internal_server
                        ERROR_INVALID_LICENSE -> msgRes =
                            R.string.err_klm_license_invalid_license
                        ERROR_INVALID_PACKAGE_NAME -> msgRes =
                            R.string.err_klm_invalid_package_name
                        ERROR_LICENSE_TERMINATED -> msgRes =
                            R.string.err_klm_license_terminated
                        ERROR_NETWORK_DISCONNECTED -> msgRes =
                            R.string.err_klm_network_disconnected
                        ERROR_NETWORK_GENERAL -> msgRes =
                            R.string.err_klm_network_general
                        ERROR_NOT_CURRENT_DATE -> msgRes =
                            R.string.err_klm_not_current_date
                        ERROR_NULL_PARAMS -> msgRes =
                            R.string.err_klm_null_params
                        ERROR_UNKNOWN -> msgRes =
                            R.string.err_klm_unknown
                        ERROR_USER_DISAGREES_LICENSE_AGREEMENT -> msgRes =
                            R.string.err_klm_user_disagrees_license_agreement
                        else -> {
                            // Unknown error code
                            val errorStatus =
                                intent.getStringExtra(EXTRA_LICENSE_STATUS)
                            val msg = context.resources.getString(
                                R.string.err_klm_code_unknown,
                                errorCode.toString(),
                                errorStatus
                            )
                            showToast(context, msg)
                            Log.d(TAG, msg)
                            return
                        }
                    }

                    // Display error message
                    showToast(context, msgRes)
                    Log.d(TAG, context.getString(msgRes))
                    return
                }
            }
        }
    }

    private fun Intent.showLicenseActivationInfo() {
        val status = getStringExtra(EXTRA_LICENSE_STATUS)
        val errorCode =
            getIntExtra(EXTRA_LICENSE_ERROR_CODE, DEFAULT_ERROR_CODE)
        val extraResultType =
            getIntExtra(EXTRA_LICENSE_RESULT_TYPE, DEFAULT_RESULT_TYPE)
        Log.d(
            TAG,
            "KLM status = $status" +
                    ", errorCode = $errorCode" +
                    ", extraResultType = ${resultTypeString(extraResultType)}"
        )
    }

    private fun resultTypeString(type: Int): String {
        return when (type) {
            DEFAULT_RESULT_TYPE -> "DEFAULT_RESULT_TYPE"
            800 -> "ACTIVATION"
            801 -> "VALIDATION"
            802 -> "DEACTIVATION"
            else -> throw IllegalArgumentException("Unknown result type: $type")
        }
    }

    companion object {
        private const val TAG = "KnoxLicenseReceiver"
        private const val DEFAULT_ERROR_CODE = -1
        private const val DEFAULT_RESULT_TYPE = -1
    }
}
