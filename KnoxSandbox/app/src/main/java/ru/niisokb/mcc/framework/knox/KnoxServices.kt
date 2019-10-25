package ru.niisokb.mcc.framework.knox

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.EnterpriseKnoxManager
import com.samsung.android.knox.container.KnoxContainerManager
import ru.niisokb.mcc.R
import kotlin.system.exitProcess


class KnoxServices {

    fun checkApiLevel(context: Context, apiLevel: Int) {
        if (EnterpriseDeviceManager.getAPILevel() < apiLevel) {
            val builder = AlertDialog.Builder(context)
            val message = context.resources.getString(
                R.string.knox_api_level_message,
                EnterpriseDeviceManager.getAPILevel(),
                apiLevel
            )
            builder.setTitle(R.string.app_name)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("CLOSE") { _, _ -> exitProcess(0) }
                .show()
        }
    }

    fun getEdm(context: Context): EnterpriseDeviceManager =
        EnterpriseDeviceManager.getInstance(context)

    fun getEkm(context: Context): EnterpriseKnoxManager =
        EnterpriseKnoxManager.getInstance(context)

    fun getKcm(context: Context): KnoxContainerManager? {
        val containerId = getContainerId()
        return if (containerId != -1) getEkm(context).getKnoxContainerManager(containerId) else null
    }

    fun getContainerId(): Int {
        val containerIds = KnoxContainerManager.getContainers()
        return if (!containerIds.isNullOrEmpty()) containerIds[0] else -1
    }
}
