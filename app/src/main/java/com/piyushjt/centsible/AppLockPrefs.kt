package com.piyushjt.centsible

import android.content.Context

object AppLockPrefs {

    private const val PREFS_NAME = "centsible_prefs"
    private const val KEY_APP_LOCK_ENABLED = "app_lock_enabled"

    fun isAppLockEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_APP_LOCK_ENABLED, false)
    }

    fun setAppLockEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_APP_LOCK_ENABLED, enabled).apply()
    }

}
