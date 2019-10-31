package ru.niisokb.mcc.di

import android.content.Context
import android.os.Build
import ru.niisokb.mcc.data.SharedPreferencesDataSourceImpl
import ru.niisokb.mcc.framework.admin.AdminActivator
import ru.niisokb.mcc.framework.dpm.DpmServices
import ru.niisokb.mcc.framework.dpm.actions.SetPackageSuspendedAction
import ru.niisokb.mcc.framework.dpm.workprofile.WorkProfileCreator
import ru.niisokb.mcc.framework.interfaces.SharedPreferencesDataSource
import ru.niisokb.mcc.framework.knox.KnoxServices
import ru.niisokb.mcc.framework.knox.actions.ToggleContainerCameraAction
import ru.niisokb.mcc.framework.knox.actions.WipeAppDataAction
import ru.niisokb.mcc.framework.knox.container.ContainerCreator
import ru.niisokb.mcc.framework.knox.license.KnoxLicenseActivator

object Dependencies {

    lateinit var appContext: Context

    val dpmServices: DpmServices by lazy { DpmServices() }

    val adminActivator: AdminActivator by lazy { AdminActivator(dpmServices) }

    val knoxLicenseActivator: KnoxLicenseActivator by lazy {
        KnoxLicenseActivator(sharedPreferencesDataSource)
    }

    val containerCreator: ContainerCreator by lazy { ContainerCreator() }

    val workProfileCreator: WorkProfileCreator by lazy { WorkProfileCreator() }

    val knoxServices: KnoxServices by lazy { KnoxServices() }

    val wipeAppDataAction: WipeAppDataAction by lazy { WipeAppDataAction(knoxServices) }

    val toggleContainerCameraAction: ToggleContainerCameraAction by lazy {
        ToggleContainerCameraAction(knoxServices)
    }

    val setPackageSuspendedAction: SetPackageSuspendedAction? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SetPackageSuspendedAction(dpmServices)
        } else {
            null
        }
    }

    private val sharedPreferencesDataSource: SharedPreferencesDataSource by lazy {
        SharedPreferencesDataSourceImpl()
    }
}
