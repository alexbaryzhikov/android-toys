package ru.niisokb.mcc

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.niisokb.mcc.di.Dependencies
import ru.niisokb.mcc.framework.admin.AdminActivator
import ru.niisokb.mcc.framework.knox.license.KPE_DEVELOP
import ru.niisokb.mcc.framework.knox.license.KPE_STANDARD
import ru.niisokb.mcc.framework.knox.license.KnoxLicenseActivator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dependencies.appContext = applicationContext
        Dependencies.knoxServices.checkApiLevel(this, 24)
        refreshPrivilegesStatus(Dependencies.adminActivator, Dependencies.knoxLicenseActivator)

        activateAdminButton.setOnClickListener {
            Dependencies.adminActivator.activateAdmin(this)
        }

        deactivateAdminButton.setOnClickListener {
            Dependencies.adminActivator.deactivateAdmin(this)
        }

        activatePremiumLicenseButton.setOnClickListener {
            Dependencies.knoxLicenseActivator.activateLicense(this, KPE_DEVELOP)
        }

        activateDeveloperLicenseButton.setOnClickListener {
            Dependencies.knoxLicenseActivator.activateLicense(this, KPE_PREMIUM)
        }

        activateStandardLicenseButton.setOnClickListener {
            Dependencies.knoxLicenseActivator.activateLicense(this, KPE_STANDARD)
        }

        deactivateLicenseButton.setOnClickListener {
            Dependencies.knoxLicenseActivator.deactivateLicense(this)
        }

        refreshStatusButton.setOnClickListener {
            refreshPrivilegesStatus(Dependencies.adminActivator, Dependencies.knoxLicenseActivator)
        }

        createContainerButton.setOnClickListener {
            Dependencies.containerCreator.createContainer(this)
        }

        createWorkProfileButton.setOnClickListener {
            Dependencies.workProfileCreator.createWorkProfile(this)
        }

        toggleContainerCameraButton.setOnClickListener {
            Dependencies.toggleContainerCameraAction.toggleCameraState(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            suspendAppButton.setOnClickListener {
                Dependencies.setPackageSuspendedAction?.suspendApp(this)
            }
        }
    }

    private fun refreshPrivilegesStatus(
        adminActivator: AdminActivator,
        knoxLicenseActivator: KnoxLicenseActivator
    ) {
        val adminActive = adminActivator.isAdminActive(this)
        Log.d(TAG, "adminActive = $adminActive")
        adminStatus.text = adminActive.toString()
        val kpeApiAccessible = knoxLicenseActivator.isKpeApiAccessible(this)
        Log.d(TAG, "kpeApiAccessible = $kpeApiAccessible")
        licenseStatus.text = kpeApiAccessible.toString()
        val ownerActive = adminActivator.isOwnerActive(this)
        Log.d(TAG, "ownerActive = $ownerActive")
        ownerStatus.text = ownerActive.toString()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
