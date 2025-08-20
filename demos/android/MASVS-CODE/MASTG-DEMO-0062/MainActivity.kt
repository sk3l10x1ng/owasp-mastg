package org.owasp.mastestapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val MASTG_TEXT_TAG = "mastgTestText"

class MainActivity : ComponentActivity() {

    private lateinit var mastgTest: MastgTest
    private lateinit var appUpdateResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the class that contains the update logic.
        mastgTest = MastgTest(applicationContext)

        // *** THE CORE ENFORCEMENT LOGIC TO PREVENT BYPASSING THE UPDATE ***
        // This launcher handles the result of the update flow initiated by `startUpdateFlowForResult`.
        appUpdateResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            // This callback is triggered when the update flow UI (the full-screen update prompt) is closed.
            // We inspect the `result.resultCode` to determine what action the user took.

            // If the user accepts the update, the result code will be `Activity.RESULT_OK`.
            // If the user cancels the update (e.g., by pressing the back button), the result code
            // will be `Activity.RESULT_CANCELED`. Other errors have different codes.
            // Therefore, checking for `!= Activity.RESULT_OK` catches all non-successful outcomes.
            if (result.resultCode != Activity.RESULT_OK) {
                Log.e(
                    "MainActivity",
                    "Update flow was cancelled or failed! Result code: ${result.resultCode}. Re-initiating."
                )

                // *** ENFORCEMENT ACTION ***
                // To ensure the user cannot proceed without updating, we immediately
                // re-trigger the update check. This creates an "enforcement loop" that
                // effectively blocks the user from accessing the app's main content
                // until they accept the mandatory update.
                mastgTest.checkForUpdate(this, appUpdateResultLauncher)

            } else {
                // This block is executed if `result.resultCode == Activity.RESULT_OK`.
                // It means the user has accepted the update. The Google Play Store is now
                // handling the download and installation. The app will be automatically
                // restarted by Play after the update is complete. No further action is needed here.
                Log.d("MainActivity", "Update accepted. The update is now in progress.")
            }
        }

        setContent {
            MainScreen(
                displayString = "App is running. Checking for mandatory updates...",
                onStartClick = {
                    // Manually trigger a check for testing purposes.
                    mastgTest.checkForUpdate(this, appUpdateResultLauncher)
                }
            )
        }

        // Trigger the initial update check when the Activity is created.
        mastgTest.checkForUpdate(this, appUpdateResultLauncher)
    }

    override fun onResume() {
        super.onResume()
        // *** THE SECOND LAYER OF ENFORCEMENT ***
        // This ensures a mandatory update isn't bypassed by the user backgrounding
        // and then returning to the app. It brings the update flow back to the foreground.
        if (::mastgTest.isInitialized) {
            mastgTest.resumeUpdateIfInProgress(this, appUpdateResultLauncher)
        }
    }
}

@Preview
@Composable
fun MainScreen(
    displayString: String = "App is running.",
    onStartClick: () -> Unit = {}
) {
    BaseScreen(onStartClick = onStartClick) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .testTag(MASTG_TEXT_TAG),
            text = displayString,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}