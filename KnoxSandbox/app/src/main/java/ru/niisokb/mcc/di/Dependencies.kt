package ru.niisokb.mcc.di

import android.content.Context
import ru.niisokb.mcc.framework.admin.AdminActivator
import ru.niisokb.mcc.framework.dpm.workprofile.WorkProfileCreator
import ru.niisokb.mcc.framework.knox.KnoxServices
import ru.niisokb.mcc.framework.knox.actions.ToggleContainerCameraAction
import ru.niisokb.mcc.framework.knox.actions.WipeAppDataAction
import ru.niisokb.mcc.framework.knox.container.ContainerCreator
import ru.niisokb.mcc.framework.knox.license.KnoxLicenseActivator

object Dependencies {

    lateinit var appContext: Context

    val adminActivator: AdminActivator by lazy { AdminActivator() }

    val knoxLicenseActivator: KnoxLicenseActivator by lazy { KnoxLicenseActivator() }

    val containerCreator: ContainerCreator by lazy { ContainerCreator() }

    val workProfileCreator: WorkProfileCreator by lazy { WorkProfileCreator() }

    val knoxServices: KnoxServices by lazy { KnoxServices() }

    val wipeAppDataAction: WipeAppDataAction by lazy { WipeAppDataAction(knoxServices) }

    val toggleContainerCameraAction: ToggleContainerCameraAction by lazy {
        ToggleContainerCameraAction(knoxServices)
    }
}
