package red.line.callino.ui.themeengine

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.ui.theme.AuroraGradient
import red.line.callino.ui.theme.CyberNeonGradient
import red.line.callino.ui.theme.DeepCosmosGradient
import red.line.callino.ui.theme.PersianGoldGradient
import red.line.callino.ui.theme.RomanticGradient
import kotlin.math.cos
import kotlin.math.sin

/**
 * GradientThemeRenderer - Renders animated gradient shaders, particles, and stylized geometric canvases.
 */
@Composable
fun GradientThemeRenderer(
    theme: CallTheme,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            theme.type == ThemeType.ANIMATION -> {
                AnimatedParticleCanvas(themeId = theme.id)
            }
            theme.type == ThemeType.VIDEO -> {
                AnimatedVideoCanvasSimulation(themeId = theme.id)
            }
            else -> {
                StaticVisualCanvas(themeId = theme.id)
            }
        }
    }
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
