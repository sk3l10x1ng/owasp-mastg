package org.owasp.mastestapp

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MastgTest(context: Context) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

    /**
     * Checks if an IMMEDIATE update is available on the Play Store.
     */
    fun checkForUpdate(
        activity: Activity,
        appUpdateResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        Log.d("MastgTest", "Checking for an update...")
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isImmediateUpdateAllowed = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

            if (isUpdateAvailable && isImmediateUpdateAllowed) {
                Log.d("MastgTest", "Immediate update available. Starting flow.")
                // Request the update. The result will be handled by the ActivityResultLauncher in MainActivity.
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    appUpdateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            } else {
                Log.d("MastgTest", "No immediate update available.")
            }
        }.addOnFailureListener { e ->
            Log.e("MastgTest", "Failed to check for updates.", e)
        }
    }

    /**
     * Resumes an update that is already in progress. This is critical for onResume().
     */
    fun resumeUpdateIfInProgress(
        activity: Activity,
        appUpdateResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                Log.d("MastgTest", "Resuming in-progress update.")
                // If an in-app update is already running, resume it.
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    appUpdateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }
}