package ru.niisokb.mcc.di

import ru.niisokb.mcc.framework.admin.AdminActivator
import ru.niisokb.mcc.framework.knox.actions.WipeAppDataAction
import ru.niisokb.mcc.framework.knox.license.KnoxLicenseActivator

object Dependencies {
    val adminActivator: AdminActivator by lazy { AdminActivator() }
    val knoxLicenseActivator: KnoxLicenseActivator by lazy { KnoxLicenseActivator() }
    val wipeAppDataAction: WipeAppDataAction by lazy { WipeAppDataAction() }
}
