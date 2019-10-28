package ru.niisokb.mcc.framework.knox.actions

import android.content.Context
import android.util.Log
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.knox.KnoxServices
import ru.niisokb.mcc.framework.utils.showToast

class ToggleContainerCameraAction(private val knoxServices: KnoxServices) {

    fun toggleCameraState(context: Context) {
        Log.d(TAG, "containerId = ${knoxServices.getContainerId()}")
        val kcm = knoxServices.getKcm(context)
        if (kcm == null) {
            Log.e(TAG, "Can't get KnoxContainerManager")
            return
        }
        val restrictionPolicy = kcm.restrictionPolicy
        val cameraEnabled = restrictionPolicy.isCameraEnabled(false)
        runCatching {
            // Disable camera. Other applications that use the camera cannot use it.
            val result = restrictionPolicy.setCameraState(!cameraEnabled)
            if (result) {
                showToast(context, R.string.toggle_camera_success)
            } else {
                showToast(context, R.string.toggle_camera_failure)
            }
        }.onFailure {
            showToast(context, R.string.toggle_camera_failure)
            Log.e(TAG, context.getString(R.string.toggle_camera_failure) + ": $it")
        }
    }

    companion object {
        private const val TAG = "ToggleContainerCamera"
    }
}
