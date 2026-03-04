package com.piyushjt.centsible

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.Job

class AppUpdateHelper(private val context: Context) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null

    /**
     * Checks for available updates and starts a Flexible update if one is found.
     */
    fun checkForUpdate(
        activity: Activity,
        updateLauncher: ActivityResultLauncher<IntentSenderRequest>,
        onUpdateDownloaded: () -> Job
    ) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Register listener to monitor the update status
                registerInstallListener(onUpdateDownloaded)

                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    )
                } catch (e: Exception) {
                    Log.e("AppUpdateHelper", "Error starting update flow", e)
                }
            }
        }
    }

    /**
     * Checks if an update is already downloaded and waiting to be installed.
     * This should be called in Activity.onResume().
     */
    fun checkUpdateStatus(onUpdateDownloaded: () -> Unit) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                onUpdateDownloaded()
            }
        }
    }

    /**
     * Registers a listener to monitor the installation progress.
     */
    private fun registerInstallListener(onUpdateDownloaded: () -> Job) {
        if (installStateUpdatedListener == null) {
            installStateUpdatedListener = InstallStateUpdatedListener { state ->
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    onUpdateDownloaded()
                }
            }
            appUpdateManager.registerListener(installStateUpdatedListener!!)
        }
    }

    /**
     * Unregisters the listener to avoid memory leaks.
     */
    fun unregisterListener() {
        installStateUpdatedListener?.let {
            appUpdateManager.unregisterListener(it)
        }
    }

    /**
     * Triggers the completion of the update (restarts the app and installs).
     */
    fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }
}
