package red.line.callino

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import red.line.callino.ui.MainScreen
import red.line.callino.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val OVERLAY_REQUEST_CODE = 1234
        private const val FULLSCREEN_INTENT_REQUEST_CODE = 1235
        private const val PREFS_NAME = "callino_prefs"
        private const val KEY_SERVICE_ENABLED = "isServiceEnabled"
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        requestOverlayPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Settings.canDrawOverlays(this) && isServiceEnabled()) {
            // همه چیز آماده است
        } else {
            requestEssentialPermissions()
        }

        setContent {
            // 🔄 راست‌چین کردن کل برنامه
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                MyApplicationTheme {
                    MainScreen()
                }
            }
        }
    }

    private fun requestEssentialPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            requestPermissionsLauncher.launch(missing.toTypedArray())
        } else {
            requestOverlayPermission()
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(
                    this,
                    "برای نمایش روی صفحه تماس، مجوز «نمایش روی برنامه‌های دیگر» را فعال کنید",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, OVERLAY_REQUEST_CODE)
            } else {
                enableServiceAutomatically()
                requestFullScreenIntentPermission()
            }
        } else {
            enableServiceAutomatically()
            requestFullScreenIntentPermission()
        }
    }

    private fun requestFullScreenIntentPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.canUseFullScreenIntent()) {
                Toast.makeText(
                    this,
                    "برای نمایش روی صفحه قفل، مجوز «Full screen notifications» را فعال کنید",
                    Toast.LENGTH_LONG
                ).show()

                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivityForResult(intent, FULLSCREEN_INTENT_REQUEST_CODE)
                } catch (e: Exception) {
                    // اگر این تنظیمات نبود
                }
            }
        }
    }

    private fun enableServiceAutomatically() {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_SERVICE_ENABLED, true)
            .apply()
    }

    private fun isServiceEnabled(): Boolean {
        return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SERVICE_ENABLED, false)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OVERLAY_REQUEST_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    Settings.canDrawOverlays(this)
                ) {
                    enableServiceAutomatically()
                    Toast.makeText(
                        this,
                        "✅ آماده است! روی صفحه تماس نمایش داده می‌شود",
                        Toast.LENGTH_SHORT
                    ).show()
                    requestFullScreenIntentPermission()
                } else {
                    Toast.makeText(
                        this,
                        "❌ بدون این مجوز برنامه روی صفحه تماس نمایش داده نمی‌شود",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            FULLSCREEN_INTENT_REQUEST_CODE -> {
                Toast.makeText(
                    this,
                    "تنظیمات ذخیره شد. حالا یک تماس آزمایشی بگیرید.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}