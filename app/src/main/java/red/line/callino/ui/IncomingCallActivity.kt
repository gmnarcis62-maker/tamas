package red.line.callino.ui

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.data.CallinoRepository
import red.line.callino.data.ContactTheme
import red.line.callino.ui.screens.CallSimulatorScreen
import red.line.callino.ui.theme.MyApplicationTheme

class IncomingCallActivity : ComponentActivity() {

    companion object {
        const val EXTRA_CALLER_NUMBER = "extra_caller_number"
        const val EXTRA_CALLER_NAME = "extra_caller_name"
    }

    private var callEndedReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Turn screen on and show over lock screen
        setupLockScreenFlags()

        val callerNumber = intent?.getStringExtra(EXTRA_CALLER_NUMBER) ?: "۰۹۱۲۳۴۵۶۷۸۹"
        val callerNameIntent = intent?.getStringExtra(EXTRA_CALLER_NAME)

        val repository = CallinoRepository.getInstance(applicationContext)

        // Register receiver to auto close when call ends
        registerCallEndReceiver()

        setContent {
            MyApplicationTheme {
                var callerName by remember { mutableStateOf(callerNameIntent ?: "تماس ورودی") }
                var relationshipLabel by remember { mutableStateOf<String?>(null) }
                var activeTheme by remember { mutableStateOf<CallTheme?>(null) }
                var settings by remember { mutableStateOf(AppSettings()) }

                LaunchedEffect(callerNumber) {
                    val currentSettings = repository.appSettings.value
                    settings = currentSettings

                    // Check for contact theme
                    val contactTheme = repository.getContactThemeForNumber(callerNumber)
                    if (contactTheme != null) {
                        callerName = contactTheme.contactName
                        relationshipLabel = contactTheme.relationshipLabel
                        activeTheme = repository.getThemeById(contactTheme.themeId)
                    }

                    if (activeTheme == null) {
                        activeTheme = repository.getThemeById(currentSettings.activeGlobalThemeId)
                            ?: repository.allThemes.firstOrNull()?.firstOrNull()
                            ?: red.line.callino.data.DefaultCallTheme
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    activeTheme?.let { theme ->
                        CallSimulatorScreen(
                            theme = theme,
                            settings = settings,
                            callerName = callerName,
                            callerNumber = callerNumber,
                            relationshipLabel = relationshipLabel,
                            isPreviewMode = false,
                            onAnswerCall = {
                                answerIncomingCall()
                            },
                            onRejectCall = {
                                endIncomingCall()
                                finish()
                            },
                            onClose = {
                                endIncomingCall()
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setupLockScreenFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }

    private fun answerIncomingCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(android.Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
                val telecomManager = getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
                telecomManager?.acceptRingingCall()
            }
        }
    }

    private fun endIncomingCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (checkSelfPermission(android.Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
                val telecomManager = getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
                telecomManager?.endCall()
            }
        }
    }

    private fun registerCallEndReceiver() {
        callEndedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
                if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                    finish()
                }
            }
        }
        val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        registerReceiver(callEndedReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        callEndedReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {}
        }
    }
}
