package ru.niisokb.mcc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.niisokb.mcc.di.Dependencies
import ru.niisokb.mcc.framework.admin.AdminActivator
import ru.niisokb.mcc.framework.knox.license.KPE1
import ru.niisokb.mcc.framework.knox.license.KnoxLicenseActivator

class MainActivity : AppCompatActivity() {

    private val gmailUid = "com.google.android.gm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adminActivator = Dependencies.adminActivator
        adminStatus.text = adminActivator.isAdminActive(this).toString()
        activateAdminButton.setOnClickListener {
            adminActivator.activateAdmin(this)
        }
        deactivateAdminButton.setOnClickListener {
            adminActivator.deactivateAdmin(this)
        }

        val knoxLicenseActivator = Dependencies.knoxLicenseActivator
        licenseStatus.text = knoxLicenseActivator.isKpeApiAccessible(this).toString()
        activateLicenseButton.setOnClickListener {
            knoxLicenseActivator.activateLicense(this, KPE1)
        }

        refreshStatusButton.setOnClickListener {
            refreshStatus(adminActivator, knoxLicenseActivator)
        }

        val containerCreator = Dependencies.containerCreator
        createContainerButton.setOnClickListener {
            containerCreator.createContainer(this)
        }

        doActionButton.setOnClickListener {
            Dependencies.wipeAppDataAction.wipeAppData(this, gmailUid)
        }
    }

    private fun refreshStatus(
        adminActivator: AdminActivator,
        knoxLicenseActivator: KnoxLicenseActivator
    ) {
        val adminActive = adminActivator.isAdminActive(this)
        Log.d(TAG, "adminActive = $adminActive")
        adminStatus.text = adminActive.toString()
        val kpeApiAccessible = knoxLicenseActivator.isKpeApiAccessible(this)
        Log.d(TAG, "kpeApiAccessible = $kpeApiAccessible")
        licenseStatus.text = kpeApiAccessible.toString()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
