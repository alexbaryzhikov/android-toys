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
        Log.v(TAG, "onReceive called")
        if (intent == null) {
            showToast(context, R.string.no_intent)
            return
        }
        val action = intent.action
        if (action == null) {
            showToast(context, R.string.no_intent_action)
            return
        }
        if (action != ACTION_LICENSE_STATUS) {
            showToast(context, R.string.invalid_intent_action)
            return
        }

        intent.logLicenseActionDetails()

        val resultType = intent.getIntExtra(EXTRA_LICENSE_RESULT_TYPE, NO_RESULT_TYPE)
        val errorCode = intent.getIntExtra(EXTRA_LICENSE_ERROR_CODE, NO_ERROR_CODE)

        val actionResId = when (resultType) {
            LICENSE_RESULT_TYPE_ACTIVATION -> R.string.license_activation
            LICENSE_RESULT_TYPE_VALIDATION -> R.string.license_validation
            LICENSE_RESULT_TYPE_DEACTIVATION -> R.string.license_deactivation
            else -> {
                Log.e(TAG, "Wrong license result type: $resultType")
                return
            }
        }

        if (errorCode == ERROR_NONE) {
            // License action successful.
            val message = context.getString(
                R.string.license_status,
                context.getString(actionResId),
                context.getString(R.string.license_success)
            )
            showToast(context, message)
            Log.d(TAG, message)
        } else {
            // License action failed.
            val msgResId = when (errorCode) {
                ERROR_INTERNAL -> R.string.err_klm_internal
                ERROR_INTERNAL_SERVER -> R.string.err_klm_internal_server
                ERROR_INVALID_LICENSE -> R.string.err_klm_license_invalid_license
                ERROR_INVALID_PACKAGE_NAME -> R.string.err_klm_invalid_package_name
                ERROR_LICENSE_TERMINATED -> R.string.err_klm_license_terminated
                ERROR_NETWORK_DISCONNECTED -> R.string.err_klm_network_disconnected
                ERROR_NETWORK_GENERAL -> R.string.err_klm_network_general
                ERROR_NOT_CURRENT_DATE -> R.string.err_klm_not_current_date
                ERROR_NULL_PARAMS -> R.string.err_klm_null_params
                ERROR_UNKNOWN -> R.string.err_klm_unknown
                ERROR_USER_DISAGREES_LICENSE_AGREEMENT -> R.string.err_klm_user_disagrees_license_agreement
                else -> null
            }
            // Display error message.
            val message = if (msgResId != null) {
                context.getString(
                    R.string.license_status,
                    context.getString(actionResId),
                    context.getString(msgResId)
                )
            } else {
                val errorStatus = intent.getStringExtra(EXTRA_LICENSE_STATUS)
                val errorMessage = context.resources.getString(
                    R.string.err_klm_code_unknown,
                    errorCode.toString(),
                    errorStatus
                )
                context.getString(
                    R.string.license_status,
                    context.getString(actionResId),
                    errorMessage
                )
            }
            showToast(context, message)
            Log.e(TAG, message)
        }
    }

    private fun Intent.logLicenseActionDetails() {
        val status = getStringExtra(EXTRA_LICENSE_STATUS)
        val errorCode =
            getIntExtra(EXTRA_LICENSE_ERROR_CODE, NO_ERROR_CODE)
        val resultType =
            resultTypeString(getIntExtra(EXTRA_LICENSE_RESULT_TYPE, NO_RESULT_TYPE))
        Log.d(TAG, "KLM status = $status, errorCode = $errorCode, resultType = $resultType")
    }

    private fun resultTypeString(type: Int): String {
        return when (type) {
            NO_RESULT_TYPE -> "NO_RESULT_TYPE"
            LICENSE_RESULT_TYPE_ACTIVATION -> "ACTIVATION"
            LICENSE_RESULT_TYPE_VALIDATION -> "VALIDATION"
            LICENSE_RESULT_TYPE_DEACTIVATION -> "DEACTIVATION"
            else -> throw IllegalArgumentException("Unknown result type: $type")
        }
    }

    companion object {
        private const val TAG = "KnoxLicenseReceiver"
        private const val NO_ERROR_CODE = -1
        private const val NO_RESULT_TYPE = -1
    }
}
