package ru.niisokb.mcc.framework.knox.container

import android.content.Context
import com.samsung.android.knox.container.KnoxContainerManager
import ru.niisokb.mcc.R
import ru.niisokb.mcc.framework.utils.showToast

class ContainerCreator {

    fun createContainer(context: Context) {
        runCatching {
            val receiver = ContainerReceiver()
            receiver.register(context)

            val initialRequestId = KnoxContainerManager.createContainer("knox-b2b")
            if (initialRequestId < 0) {
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
}