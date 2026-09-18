package red.line.callino.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.ui.components.CallButtonsContainer
import red.line.callino.ui.components.PulsingRings
import red.line.callino.ui.effects.CallerInfoGlassCard
import red.line.callino.ui.themeengine.CallThemeRenderer
import red.line.callino.ui.theme.CallinoDanger
import red.line.callino.ui.theme.CallinoSuccess
import red.line.callino.ui.theme.FrostedPrimary
import kotlin.math.roundToInt

@Composable
fun CallSimulatorScreen(
    theme: CallTheme,
    settings: AppSettings,
    callerName: String = "",
    callerNumber: String = "",
    relationshipLabel: String? = null,
    contactPhotoUri: String? = null,
    isPreviewMode: Boolean = false,
    onAnswerCall: (() -> Unit)? = null,
    onRejectCall: (() -> Unit)? = null,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCallAnswered by remember { mutableStateOf(false) }
    var callSeconds by remember { mutableIntStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }

    // Vibration handling
    LaunchedEffect(settings.vibrateOnCall, isCallAnswered) {
        if (settings.vibrateOnCall && !isCallAnswered) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 500, 700, 500, 700)
                    val amplitudes = intArrayOf(0, 180, 0, 180, 0)
                    vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, 0))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 500, 700), 0)
                }
            } catch (e: Exception) {}
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vibratorManager?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                vibrator?.cancel()
            } catch (e: Exception) {}
        }
    }

    // Call Duration Timer
    LaunchedEffect(isCallAnswered) {
        if (isCallAnswered) {
            try {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vibratorManager?.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                }
                vibrator?.cancel()
            } catch (e: Exception) {}

            while (true) {
                delay(1000)
                callSeconds++
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("call_simulator_screen")
    ) {
        CallThemeRenderer(
            theme = theme,
            settings = settings
        )

        // Close Button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(top = 42.dp, end = 20.dp)
                .align(Alignment.TopEnd)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .testTag("close_simulator_btn")
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "بستن پیش‌نمایش",
                tint = Color.White
            )
        }

        // Caller Info Block
        val infoAlignment = when (settings.callerInfoPosition) {
            "CENTER" -> Alignment.Center
            "BOTTOM" -> Alignment.BottomCenter
            else -> Alignment.TopCenter
        }

        val topPadding = when (settings.callerInfoPosition) {
            "CENTER" -> 0.dp
            "BOTTOM" -> 0.dp
            else -> 64.dp
        }

        Box(
            modifier = Modifier
                .align(infoAlignment)
                .padding(top = topPadding, bottom = if (settings.callerInfoPosition == "BOTTOM") 190.dp else 0.dp)
                .fillMaxWidth()
        ) {
            CallerInfoGlassCard(
                callerName = callerName,
                callerNumber = callerNumber,
                relationshipLabel = relationshipLabel,
                contactPhotoUri = contactPhotoUri,
                isPreviewMode = isPreviewMode,
                isCallAnswered = isCallAnswered,
                callDurationSeconds = callSeconds,
                settings = settings
            )
        }

        // Call Action Buttons
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 54.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {
            if (!isCallAnswered) {
                CallButtonsContainer(
                    settings = settings,
                    onAnswerClick = {
                        onAnswerCall?.invoke()
                        isCallAnswered = true
                    },
                    onRejectClick = {
                        onRejectCall?.invoke() ?: onClose()
                    }
                )
            } else {
                ActiveCallControls(
                    isMuted = isMuted,
                    isSpeakerOn = isSpeakerOn,
                    onToggleMute = { isMuted = !isMuted },
                    onToggleSpeaker = { isSpeakerOn = !isSpeakerOn },
                    onEndCall = onClose
                )
            }
        }
    }
}

@Composable
private fun ActiveCallControls(
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onEndCall: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggleMute,
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(if (isMuted) Color.White else Color.White.copy(alpha = 0.25f))
                    .testTag("toggle_mute_btn")
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "بی‌صدا",
                    tint = if (isMuted) Color.Black else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(
                onClick = onToggleSpeaker,
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(if (isSpeakerOn) Color.White else Color.White.copy(alpha = 0.25f))
                    .testTag("toggle_speaker_btn")
            ) {
                Icon(
                    imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                    contentDescription = "بلندگو",
                    tint = if (isSpeakerOn) Color.Black else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        IconButton(
            onClick = onEndCall,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(CallinoDanger)
                .testTag("end_active_call_btn")
        ) {
            Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "پایان تماس",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}