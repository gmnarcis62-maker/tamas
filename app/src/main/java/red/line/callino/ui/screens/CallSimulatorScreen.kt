package red.line.callino.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.ui.components.CallButtonsContainer
import red.line.callino.ui.components.CallThemeBackground
import red.line.callino.ui.components.PulsingRings
import red.line.callino.ui.effects.CallerInfoGlassCard
import red.line.callino.ui.themeengine.CallThemeRenderer
import red.line.callino.ui.theme.CallinoDanger
import red.line.callino.ui.theme.CallinoPrimary
import red.line.callino.ui.theme.CallinoSuccess
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep
import red.line.callino.ui.theme.FrostedTintLight
import kotlin.math.roundToInt

@Composable
fun CallSimulatorScreen(
    theme: CallTheme,
    settings: AppSettings,
    callerName: String = "تماسینو",
    callerNumber: String = "",
    relationshipLabel: String? = "پیش‌نمایش تم",
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
            // Stop vibrator
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
        // Dynamic Live Animated Background with Effects Layers
        CallThemeRenderer(
            theme = theme,
            settings = settings
        )

        // Close preview icon (top-left / top-right)
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

        // Caller Info Block (Configurable position: TOP, CENTER, BOTTOM)
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

        // Call Action Buttons (Bottom Bar)
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
                // Active Call Controls (Mute, Speaker, End)
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
private fun ModernCallButtons(
    animationsEnabled: Boolean,
    onAnswer: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Decline Call (Red)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onReject() }.testTag("reject_call_btn")
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(CallinoDanger),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = "رد تماس",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "رد تماس", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        // Answer Call (Green with pulses)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onAnswer() }.testTag("answer_call_btn")
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (animationsEnabled) {
                    PulsingRings(color = CallinoSuccess)
                }

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(CallinoSuccess),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "پاسخ",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "پاسخ", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ClassicCallButtons(
    onAnswer: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Reject button
        Card(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable { onReject() }
                .testTag("classic_reject_btn"),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = CallinoDanger)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CallEnd, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "رد تماس", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        // Answer button
        Card(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable { onAnswer() }
                .testTag("classic_answer_btn"),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = CallinoSuccess)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Call, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "پاسخ به تماس", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun SwipeCallButtons(
    onAnswer: () -> Unit,
    onReject: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .clip(RoundedCornerShape(34.dp))
            .background(Color.White.copy(alpha = 0.25f))
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(34.dp))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "← کشیدن برای پاسخ", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Text(text = "کشیدن برای رد →", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }

        // Draggable Knob
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(-140f, 140f)
                    },
                    onDragStopped = {
                        if (offsetX < -90f) {
                            onAnswer()
                        } else if (offsetX > 90f) {
                            onReject()
                        } else {
                            coroutineScope.launch {
                                val anim = Animatable(offsetX)
                                anim.animateTo(0f, tween(300))
                                offsetX = anim.value
                            }
                        }
                    }
                )
                .testTag("swipe_knob"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "سوایپ",
                tint = FrostedPrimary,
                modifier = Modifier.size(28.dp)
            )
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
            // Mute Button
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

            // Speaker Button
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

        // End Call (Hang up) Button
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
                contentDescription = "قطع تماس",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
