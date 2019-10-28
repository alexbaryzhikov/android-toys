package ru.niisokb.mcc.framework.interfaces

import android.content.Context

interface SharedPreferencesDataSource {

    fun save(context: Context, key: String, value: String)

    fun load(context: Context, key: String, default: String? = null): String?
}
