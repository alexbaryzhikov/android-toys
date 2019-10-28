package ru.niisokb.mcc.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.niisokb.mcc.framework.interfaces.SharedPreferencesDataSource

class SharedPreferencesDataSourceImpl : SharedPreferencesDataSource {

    override fun save(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun load(context: Context, key: String, default: String?): String? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        return sharedPreferences.getString(key, default)
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "common_shared_preferences"
    }
}