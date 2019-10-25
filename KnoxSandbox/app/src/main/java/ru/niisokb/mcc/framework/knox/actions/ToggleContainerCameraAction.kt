package ru.niisokb.mcc.framework.knox.actions

import android.content.Context
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.knox.KnoxServices
import ru.niisokb.mcc.framework.utils.showToast

class ToggleContainerCameraAction(private val knoxServices: KnoxServices) {

    fun toggleCameraState(context: Context) {
        val kcm = knoxServices.getKcm(context) ?: return
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
        }
    }
}
