package red.line.callino.ui.components

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.ui.theme.AuroraGradient
import red.line.callino.ui.theme.CallinoDanger
import red.line.callino.ui.theme.CallinoPrimary
import red.line.callino.ui.theme.CallinoSuccess
import red.line.callino.ui.theme.CyberNeonGradient
import red.line.callino.ui.theme.DeepCosmosGradient
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextMuted
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep
import red.line.callino.ui.theme.FrostedTintLight
import red.line.callino.ui.theme.PersianGoldGradient
import red.line.callino.ui.theme.RomanticGradient
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CallThemeBackground(
    theme: CallTheme,
    dimAlpha: Float = 0.3f,
    modifier: Modifier = Modifier
) {
    red.line.callino.ui.themeengine.CallThemeRenderer(
        theme = theme,
        dimAlpha = dimAlpha,
        modifier = modifier
    )
}

@Composable
fun AnimatedParticleCanvas(
    themeId: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "canvas_anim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val gradientColors = when (themeId) {
        "theme_cyber_neon" -> CyberNeonGradient
        "theme_romantic_glow" -> RomanticGradient
        else -> AuroraGradient
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Background Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    gradientColors[0].copy(alpha = 0.85f),
                    gradientColors[1].copy(alpha = 0.9f),
                    gradientColors.getOrNull(2) ?: Color(0xFF0F172A)
                )
            )
        )

        // Animated Light Orbs
        val rad = Math.toRadians(phase.toDouble())
        val orb1X = width * 0.5f + (cos(rad) * width * 0.3f).toFloat()
        val orb1Y = height * 0.4f + (sin(rad) * height * 0.2f).toFloat()

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.45f), Color.Transparent),
                center = Offset(orb1X, orb1Y),
                radius = width * 0.45f
            ),
            radius = width * 0.45f,
            center = Offset(orb1X, orb1Y)
        )

        val orb2X = width * 0.5f + (cos(rad + Math.PI) * width * 0.35f).toFloat()
        val orb2Y = height * 0.6f + (sin(rad + Math.PI) * height * 0.25f).toFloat()

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(gradientColors[1].copy(alpha = 0.5f), Color.Transparent),
                center = Offset(orb2X, orb2Y),
                radius = width * 0.55f
            ),
            radius = width * 0.55f,
            center = Offset(orb2X, orb2Y)
        )
    }
}

@Composable
fun AnimatedVideoCanvasSimulation(
    themeId: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "video_sim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF8B5CF6), Color(0xFF1E1B4B), Color.Black),
                center = Offset(width / 2, height / 2),
                radius = width * pulse
            )
        )

        for (i in 1..20) {
            val angle = (i * 18).toDouble()
            val r = (width * 0.35f) * ((i % 5 + 1) / 5f)
            val px = width / 2 + (cos(Math.toRadians(angle)) * r).toFloat()
            val py = height / 2 + (sin(Math.toRadians(angle)) * r).toFloat()

            drawCircle(
                color = Color.White.copy(alpha = (0.2f + (i % 4) * 0.15f)),
                radius = 3f * (i % 3 + 1),
                center = Offset(px, py)
            )
        }
    }
}

@Composable
fun StaticVisualCanvas(
    themeId: String,
    modifier: Modifier = Modifier
) {
    val colors = when (themeId) {
        "theme_persian_gold" -> PersianGoldGradient
        "theme_deep_cosmos" -> DeepCosmosGradient
        else -> listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155))
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset.Zero,
                end = Offset(size.width, size.height)
            )
        )

        if (themeId == "theme_persian_gold") {
            drawCircle(
                color = Color(0xFFD4AF37).copy(alpha = 0.15f),
                radius = size.width * 0.7f,
                center = Offset(size.width / 2, size.height / 3),
                style = Stroke(width = 4f)
            )
            drawCircle(
                color = Color(0xFFD4AF37).copy(alpha = 0.25f),
                radius = size.width * 0.5f,
                center = Offset(size.width / 2, size.height / 3),
                style = Stroke(width = 2f)
            )
        }
    }
}

@Composable
fun PulsingRings(
    color: Color = CallinoSuccess,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .size(76.dp * scale)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}
