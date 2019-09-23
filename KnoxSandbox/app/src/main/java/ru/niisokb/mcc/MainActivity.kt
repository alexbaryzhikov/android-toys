package ru.niisokb.mcc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.niisokb.mcc.di.Dependencies
import ru.niisokb.mcc.framework.knox.license.KPE1

class MainActivity : AppCompatActivity() {

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

        refreshStatus.setOnClickListener {
            val adminActive = adminActivator.isAdminActive(this)
            Log.d(TAG, "adminActive = $adminActive")
            adminStatus.text = adminActive.toString()
            val kpeApiAccessible = knoxLicenseActivator.isKpeApiAccessible(this)
            Log.d(TAG, "kpeApiAccessible = $kpeApiAccessible")
            licenseStatus.text = kpeApiAccessible.toString()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
