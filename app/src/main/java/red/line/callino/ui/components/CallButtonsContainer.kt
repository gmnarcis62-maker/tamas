package red.line.callino.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import red.line.callino.data.AppSettings
import red.line.callino.ui.effects.ConcentricPulseRings
import red.line.callino.ui.effects.GlowEffect
import red.line.callino.ui.theme.CallinoDanger
import red.line.callino.ui.theme.CallinoSuccess

/**
 * CallButtonsContainer - Component purely dedicated to rendering customizable Answer & Reject buttons.
 * Supports:
 * - 6 Visual Styles: GLASS, CRYSTAL, NEON, ROUNDED, PILL, MINIMAL
 * - 4 Layout Arrangements: BOTTOM_SIDES, BOTTOM_CENTER, SCREEN_SIDES, VERTICAL
 * - Custom Emoji & Text
 * - Dynamic Sizing: SMALL, MEDIUM, LARGE
 */
@Composable
fun CallButtonsContainer(
    settings: AppSettings,
    onAnswerClick: () -> Unit,
    onRejectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sizeConfig = when (settings.buttonSize.uppercase()) {
        "SMALL" -> ButtonSizeConfig(
            boxSize = 58.dp,
            pillHeight = 48.dp,
            pillWidth = 140.dp,
            iconSize = 26.dp,
            emojiSize = 20.sp,
            textSize = 12.sp,
            spacer = 6.dp
        )
        "LARGE" -> ButtonSizeConfig(
            boxSize = 84.dp,
            pillHeight = 64.dp,
            pillWidth = 180.dp,
            iconSize = 38.dp,
            emojiSize = 30.sp,
            textSize = 16.sp,
            spacer = 10.dp
        )
        else -> ButtonSizeConfig( // MEDIUM
            boxSize = 72.dp,
            pillHeight = 56.dp,
            pillWidth = 160.dp,
            iconSize = 32.dp,
            emojiSize = 24.sp,
            textSize = 14.sp,
            spacer = 8.dp
        )
    }

    when (settings.buttonLayout.uppercase()) {
        "VERTICAL" -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Answer Button (Top)
                SingleCallButton(
                    isAnswer = true,
                    settings = settings,
                    text = settings.answerText,
                    emoji = settings.answerEmoji,
                    animationsEnabled = settings.animationsEnabled,
                    sizeConfig = sizeConfig,
                    onClick = onAnswerClick,
                    tag = "answer_call_btn"
                )

                // Reject Button (Bottom)
                SingleCallButton(
                    isAnswer = false,
                    settings = settings,
                    text = settings.rejectText,
                    emoji = settings.rejectEmoji,
                    animationsEnabled = false,
                    sizeConfig = sizeConfig,
                    onClick = onRejectClick,
                    tag = "reject_call_btn"
                )
            }
        }

        "BOTTOM_CENTER" -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reject Button
                SingleCallButton(
                    isAnswer = false,
                    settings = settings,
                    text = settings.rejectText,
                    emoji = settings.rejectEmoji,
                    animationsEnabled = false,
                    sizeConfig = sizeConfig,
                    onClick = onRejectClick,
                    tag = "reject_call_btn"
                )

                Spacer(modifier = Modifier.width(32.dp))

                // Answer Button
                SingleCallButton(
                    isAnswer = true,
                    settings = settings,
                    text = settings.answerText,
                    emoji = settings.answerEmoji,
                    animationsEnabled = settings.animationsEnabled,
                    sizeConfig = sizeConfig,
                    onClick = onAnswerClick,
                    tag = "answer_call_btn"
                )
            }
        }

        "SCREEN_SIDES" -> {
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reject Button (Left / Start edge)
                SingleCallButton(
                    isAnswer = false,
                    settings = settings,
                    text = settings.rejectText,
                    emoji = settings.rejectEmoji,
                    animationsEnabled = false,
                    sizeConfig = sizeConfig,
                    onClick = onRejectClick,
                    tag = "reject_call_btn"
                )

                // Answer Button (Right / End edge)
                SingleCallButton(
                    isAnswer = true,
                    settings = settings,
                    text = settings.answerText,
                    emoji = settings.answerEmoji,
                    animationsEnabled = settings.animationsEnabled,
                    sizeConfig = sizeConfig,
                    onClick = onAnswerClick,
                    tag = "answer_call_btn"
                )
            }
        }

        else -> { // "BOTTOM_SIDES" (Default standard)
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reject Button
                SingleCallButton(
                    isAnswer = false,
                    settings = settings,
                    text = settings.rejectText,
                    emoji = settings.rejectEmoji,
                    animationsEnabled = false,
                    sizeConfig = sizeConfig,
                    onClick = onRejectClick,
                    tag = "reject_call_btn"
                )

                // Answer Button
                SingleCallButton(
                    isAnswer = true,
                    settings = settings,
                    text = settings.answerText,
                    emoji = settings.answerEmoji,
                    animationsEnabled = settings.animationsEnabled,
                    sizeConfig = sizeConfig,
                    onClick = onAnswerClick,
                    tag = "answer_call_btn"
                )
            }
        }
    }
}

private data class ButtonSizeConfig(
    val boxSize: Dp,
    val pillHeight: Dp,
    val pillWidth: Dp,
    val iconSize: Dp,
    val emojiSize: TextUnit,
    val textSize: TextUnit,
    val spacer: Dp
)

@Composable
private fun SingleCallButton(
    isAnswer: Boolean,
    settings: AppSettings,
    text: String,
    emoji: String,
    animationsEnabled: Boolean,
    sizeConfig: ButtonSizeConfig,
    onClick: () -> Unit,
    tag: String
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val baseGreen = Color(0xFF22C55E)
    val baseRed = Color(0xFFEF4444)
    val mainColor = if (isAnswer) baseGreen else baseRed

    // Pulse animation for answer button if enabled
    val pulseScale = remember { Animatable(1f) }
    val pressScale = remember { Animatable(1f) }

    LaunchedEffect(animationsEnabled, isAnswer) {
        if (animationsEnabled && isAnswer) {
            pulseScale.animateTo(
                targetValue = 1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            pulseScale.snapTo(1f)
        }
    }

    val triggerClick = {
        if (settings.enableHaptic) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        coroutineScope.launch {
            pressScale.animateTo(0.88f, spring(dampingRatio = 0.4f, stiffness = 400f))
            pressScale.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = 400f))
        }
        onClick()
    }

    val normalizedStyle = settings.buttonStyle.uppercase()

    if (normalizedStyle == "PILL") {
        // Horizontal Pill Capsule
        Box(
            modifier = Modifier
                .scale(pulseScale.value * pressScale.value)
                .height(sizeConfig.pillHeight)
                .width(sizeConfig.pillWidth)
                .clip(RoundedCornerShape(sizeConfig.pillHeight / 2))
                .background(
                    Brush.horizontalGradient(
                        if (isAnswer) listOf(Color(0xFF10B981), Color(0xFF059669))
                        else listOf(Color(0xFFF43F5E), Color(0xFFE11D48))
                    )
                )
                .border(
                    BorderStroke(1.5.dp, Color.White.copy(alpha = 0.6f)),
                    RoundedCornerShape(sizeConfig.pillHeight / 2)
                )
                .clickable(onClick = triggerClick)
                .testTag(tag),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                if (emoji.isNotBlank()) {
                    Text(text = emoji, fontSize = sizeConfig.emojiSize)
                } else {
                    Icon(
                        imageVector = if (isAnswer) Icons.Default.Call else Icons.Default.CallEnd,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(sizeConfig.iconSize * 0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(sizeConfig.spacer))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = sizeConfig.textSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    } else {
        // Circular / Squircle / Outlined styles
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(pulseScale.value * pressScale.value)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = triggerClick
                )
                .testTag(tag)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Glow effect background
                if (settings.enableGlow) {
                    GlowEffect(
                        glowColor = mainColor,
                        radiusDp = sizeConfig.boxSize * 0.75f,
                        pulsing = animationsEnabled && isAnswer
                    )
                }

                // Multi-stage pulsing rings
                if (animationsEnabled && isAnswer && normalizedStyle !in listOf("MINIMAL", "PILL")) {
                    ConcentricPulseRings(
                        ringColor = mainColor.copy(alpha = 0.6f),
                        maxRadiusDp = sizeConfig.boxSize * 0.85f,
                        ringCount = 3
                    )
                }

                when (normalizedStyle) {
                    "CRYSTAL" -> {
                        // Crystal Diamond-Crisp Glass
                        Box(
                            modifier = Modifier
                                .size(sizeConfig.boxSize)
                                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = mainColor)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            mainColor.copy(alpha = 0.75f),
                                            Color.White.copy(alpha = 0.35f),
                                            mainColor.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                                .border(
                                    BorderStroke(2.dp, Brush.sweepGradient(listOf(Color.White, mainColor, Color.White))),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ButtonInnerIcon(isAnswer, emoji, sizeConfig)
                        }
                    }

                    "NEON" -> {
                        // Cyberpunk Neon Glow
                        Box(
                            modifier = Modifier
                                .size(sizeConfig.boxSize)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.75f))
                                .border(
                                    BorderStroke(3.dp, if (isAnswer) Color(0xFF00FFA3) else Color(0xFFFF0055)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ButtonInnerIcon(
                                isAnswer = isAnswer,
                                emoji = emoji,
                                sizeConfig = sizeConfig,
                                tint = if (isAnswer) Color(0xFF00FFA3) else Color(0xFFFF3366)
                            )
                        }
                    }

                    "ROUNDED" -> {
                        // Smooth Squircle Solid / Deep Gradient
                        Box(
                            modifier = Modifier
                                .size(sizeConfig.boxSize)
                                .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = mainColor)
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.verticalGradient(
                                        if (isAnswer) listOf(Color(0xFF22C55E), Color(0xFF15803D))
                                        else listOf(Color(0xFFEF4444), Color(0xFFB91C1C))
                                    )
                                )
                                .border(
                                    BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                                    RoundedCornerShape(22.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ButtonInnerIcon(isAnswer, emoji, sizeConfig)
                        }
                    }

                    "MINIMAL" -> {
                        // Ultra-Sleek Transparent Wireframe
                        Box(
                            modifier = Modifier
                                .size(sizeConfig.boxSize)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f))
                                .border(
                                    BorderStroke(1.5.dp, Color.White.copy(alpha = 0.8f)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ButtonInnerIcon(isAnswer, emoji, sizeConfig, tint = Color.White)
                        }
                    }

                    else -> { // "GLASS" (Default Frosted Glass)
                        Box(
                            modifier = Modifier
                                .size(sizeConfig.boxSize)
                                .shadow(8.dp, CircleShape, spotColor = mainColor.copy(alpha = 0.6f))
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            mainColor.copy(alpha = 0.85f),
                                            mainColor.copy(alpha = 0.65f)
                                        )
                                    )
                                )
                                .border(
                                    BorderStroke(2.dp, Color.White.copy(alpha = 0.7f)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            ButtonInnerIcon(isAnswer, emoji, sizeConfig)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(sizeConfig.spacer))

            Text(
                text = text,
                color = Color.White,
                fontSize = sizeConfig.textSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ButtonInnerIcon(
    isAnswer: Boolean,
    emoji: String,
    sizeConfig: ButtonSizeConfig,
    tint: Color = Color.White
) {
    if (emoji.isNotBlank()) {
        Text(
            text = emoji,
            fontSize = sizeConfig.emojiSize,
            textAlign = TextAlign.Center
        )
    } else {
        Icon(
            imageVector = if (isAnswer) Icons.Default.Call else Icons.Default.CallEnd,
            contentDescription = if (isAnswer) "پاسخ" else "رد تماس",
            tint = tint,
            modifier = Modifier.size(sizeConfig.iconSize)
        )
    }
}
