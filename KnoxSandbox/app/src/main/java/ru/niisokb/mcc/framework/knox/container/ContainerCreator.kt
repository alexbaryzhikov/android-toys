package ru.niisokb.mcc.framework.knox.container

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.samsung.android.knox.container.CreationParams
import com.samsung.android.knox.container.KnoxContainerManager
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class ContainerCreator {

    private val warrantyBitOk: Boolean
        get() = runCatching {
            @SuppressLint("PrivateApi")
            val clazz = Class.forName("android.os.SystemProperties")
            val method = clazz.getDeclaredMethod("get", String::class.java)
            method.isAccessible = true
            var b = method.invoke(null, "ro.boot.warranty_bit") as String
            if (TextUtils.isEmpty(b))
                b = method.invoke(null, "ro.warranty_bit") as String
            b == "0"
        }.getOrDefault(false)

    fun createContainer(context: Context) {
        Log.d(TAG, "Warranty bit ok: $warrantyBitOk")
        runCatching {
            val receiver = ContainerReceiver()
            receiver.register(context)
            val creationParams = CreationParams().apply {
                setConfigurationName(CONTAINER_TYPE)
                setPasswordResetToken(PASSWORD_RESET_TOKEN)
                setAdminPackageName(context.packageName)
            }
            Log.d(TAG, "Request container creation: $CONTAINER_TYPE")

            val initialRequestId = KnoxContainerManager.createContainer(creationParams)

            if (initialRequestId < 0) {
                Log.e(TAG, "Container creation request failed, error code: $initialRequestId")
                showToast(
                    context,
                    context.getString(R.string.container_creation_error, initialRequestId)
                )
                receiver.unregister(context)
            } else {
                receiver.initialRequestId = initialRequestId
                showToast(context, context.getString(R.string.container_creation_progress))
            }
        }
    }

    companion object {
        private const val TAG = "ContainerCreator"
        private const val CONTAINER_TYPE = "knox-b2b"
        private const val PASSWORD_RESET_TOKEN = "soExR47kmmgdxUpWttBsq5hfcoZHDZM6k56VDQ04"
    }
}