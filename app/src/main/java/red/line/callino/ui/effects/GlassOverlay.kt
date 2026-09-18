package red.line.callino.ui.effects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import red.line.callino.data.AppSettings
import red.line.callino.ui.theme.CallinoPrimary
import red.line.callino.ui.theme.CallinoSecondary
import red.line.callino.ui.theme.CallinoSuccess

/**
 * CinematicVignetteOverlay - Darkens edges of the screen for a cinematic depth of field.
 */
@Composable
fun CinematicVignetteOverlay(
    modifier: Modifier = Modifier,
    vignetteColor: Color = Color.Black.copy(alpha = 0.65f)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val maxRadius = (width.coerceAtLeast(height)) * 0.75f

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    vignetteColor.copy(alpha = 0.35f),
                    vignetteColor
                ),
                center = Offset(width / 2f, height / 2f),
                radius = maxRadius
            )
        )
    }
}

/**
 * GradientGlassOverlay - Multi-layer frosted glass shading over video/image backgrounds.
 */
@Composable
fun GradientGlassOverlay(
    dimAlpha: Float = 0.3f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = (dimAlpha + 0.3f).coerceIn(0f, 0.9f)),
                        Color.Black.copy(alpha = dimAlpha.coerceIn(0f, 0.8f)),
                        Color.Black.copy(alpha = (dimAlpha + 0.45f).coerceIn(0f, 0.95f))
                    )
                )
            )
    )
}

/**
 * CallerInfoGlassCard - Premium Glassmorphism Card for incoming caller details.
 * Features:
 * - Animated slide-in from top with smooth fade-in
 * - Ambient avatar glow & pulse rings
 * - Frosted blur background card
 * - Relationship badge support
 * - Dynamic typography scaling
 */
@Composable
fun CallerInfoGlassCard(
    callerName: String,
    callerNumber: String,
    relationshipLabel: String? = null,
    contactPhotoUri: String? = null,
    isPreviewMode: Boolean = false,
    isCallAnswered: Boolean = false,
    callDurationSeconds: Int = 0,
    settings: AppSettings,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { -it / 2 },
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(durationMillis = 600)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            // Caller Avatar with Glowing Border & Pulse
            Box(contentAlignment = Alignment.Center) {
                if (settings.animationsEnabled && !isCallAnswered) {
                    if (settings.enableGlow) {
                        GlowEffect(
                            glowColor = CallinoPrimary.copy(alpha = 0.5f),
                            radiusDp = 64.dp
                        )
                    }
                    ConcentricPulseRings(
                        ringColor = Color.White.copy(alpha = 0.35f),
                        maxRadiusDp = 60.dp,
                        ringCount = 2
                    )
                }

                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CallinoPrimary.copy(alpha = 0.85f), CallinoSecondary.copy(alpha = 0.85f))
                            )
                        )
                        .border(2.5.dp, Color.White.copy(alpha = 0.85f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPreviewMode) {
                        // In Preview Mode: show stylized Callino emblem / icon without personal photo
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = "پیش‌نمایش تماسینو",
                            tint = Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    } else if (!contactPhotoUri.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(contactPhotoUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = callerName,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Frosted Glass Info Container
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.12f),
                border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.25f)),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp)
                ) {
                    // Preview Badge or Relationship Badge
                    if (isPreviewMode) {
                        Surface(
                            color = CallinoPrimary.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f))
                        ) {
                            Text(
                                text = "پیش‌نمایش زنده تم",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    } else if (!relationshipLabel.isNullOrBlank()) {
                        Surface(
                            color = CallinoPrimary.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = relationshipLabel,
                                color = Color.White,
                                fontSize = (12 * settings.fontSizeScale).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Display Name (In Preview Mode: "تماسینو")
                    val displayName = if (isPreviewMode) "تماسینو" else callerName
                    if (settings.callerNameVisible || isPreviewMode) {
                        Text(
                            text = displayName,
                            color = Color.White,
                            fontSize = (24 * settings.fontSizeScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Display Number (In Preview Mode: hidden or preview subtitle)
                    if (!isPreviewMode && settings.callerNumberVisible && !isCallAnswered && callerNumber.isNotBlank()) {
                        Text(
                            text = callerNumber,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = (15 * settings.fontSizeScale).sp,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Active Call Status / Duration Indicator
                    Surface(
                        color = if (isCallAnswered) CallinoSuccess.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            0.8.dp,
                            if (isCallAnswered) CallinoSuccess else Color.White.copy(alpha = 0.25f)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            if (isCallAnswered) {
                                Icon(
                                    imageVector = Icons.Default.PhoneInTalk,
                                    contentDescription = null,
                                    tint = Color(0xFF86EFAC),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                val minutes = callDurationSeconds / 60
                                val seconds = callDurationSeconds % 60
                                Text(
                                    text = if (isPreviewMode) {
                                        String.format("مکالمه آزمایشی %02d:%02d", minutes, seconds)
                                    } else {
                                        String.format("در حال مکالمه %02d:%02d", minutes, seconds)
                                    },
                                    color = Color(0xFF86EFAC),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Text(
                                    text = if (isPreviewMode) "پیش‌نمایش تم" else "تماس ورودی...",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
