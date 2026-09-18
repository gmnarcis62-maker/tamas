package red.line.callino.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import red.line.callino.service.CallNotificationHelper
import red.line.callino.ui.IncomingCallActivity

class IncomingCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("callino_prefs", Context.MODE_PRIVATE)
        val isServiceEnabled = prefs.getBoolean("isServiceEnabled", false)

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) ?: "۰۹۱۲۳۴۵۶۷۸۹"

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Only launch custom call screen if user explicitly enabled Callino service
                    if (!isServiceEnabled) {
                        return
                    }

                    // Incoming call ringing - launch full-screen notification / activity
                    try {
                        CallNotificationHelper.showIncomingCallNotification(
                            context = context,
                            callerNumber = incomingNumber,
                            callerName = null
                        )
                    } catch (e: Exception) {
                        // Ignored if notification permission missing
                    }

                    try {
                        val callIntent = Intent(context, IncomingCallActivity::class.java).apply {
                            putExtra(IncomingCallActivity.EXTRA_CALLER_NUMBER, incomingNumber)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        }
                        context.startActivity(callIntent)
                    } catch (e: Exception) {
                        // Managed via fullScreenIntent notification
                    }
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Call answered
                    CallNotificationHelper.cancelIncomingCallNotification(context)
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Call ended / missed
                    CallNotificationHelper.cancelIncomingCallNotification(context)
                }
            }
        }
    }
}
