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

        mastgTest = MastgTest(applicationContext)

        appUpdateResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->

            if (result.resultCode != Activity.RESULT_OK) {
                Log.e(
                    "MainActivity",
                    "Update flow was cancelled or failed! Result code: ${result.resultCode}. Re-initiating."
                )

                mastgTest.checkForUpdate(this, appUpdateResultLauncher)

            } else {
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