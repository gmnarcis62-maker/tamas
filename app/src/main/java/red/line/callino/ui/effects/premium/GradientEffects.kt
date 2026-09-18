package red.line.callino.ui.effects.premium

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MeshGradientEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "mesh")
    val phase by t.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Restart),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val r = Math.toRadians(phase.toDouble())
        val a = 0.7f * intensity

        fun drawOrb(color: Color, cx: Float, cy: Float, radius: Float) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = a), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = radius
                ),
                radius = radius,
                center = Offset(cx, cy)
            )
        }

        drawOrb(Color(0xFF8B5CF6), w * 0.3f + cos(r).toFloat() * w * 0.15f, h * 0.3f + sin(r).toFloat() * h * 0.1f, w * 0.55f)
        drawOrb(Color(0xFFEC4899), w * 0.75f + sin(r).toFloat() * w * 0.15f, h * 0.25f + cos(r).toFloat() * h * 0.1f, w * 0.55f)
        drawOrb(Color(0xFF06B6D4), w * 0.55f + cos(r * 0.7).toFloat() * w * 0.2f, h * 0.75f, w * 0.55f)
        drawOrb(Color(0xFFF59E0B), w * 0.2f, h * 0.8f + sin(r * 0.8).toFloat() * h * 0.1f, w * 0.5f)
        drawOrb(Color(0xFF10B981), w * 0.85f, h * 0.7f, w * 0.5f)
    }
}

@Composable
fun InkFlowEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "ink")
    val p1 by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p1"
    )
    val p2 by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(13000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p2"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.65f * intensity

        val cx1 = w * (0.25f + 0.5f * p1)
        val cy1 = h * (0.2f + 0.6f * (1f - p1))
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF8B5CF6).copy(a), Color.Transparent),
                center = Offset(cx1, cy1),
                radius = w * 0.55f
            ),
            radius = w * 0.55f,
            center = Offset(cx1, cy1)
        )

        val cx2 = w * (0.75f - 0.5f * p2)
        val cy2 = h * (0.7f - 0.4f * p2)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF06B6D4).copy(a * 0.9f), Color.Transparent),
                center = Offset(cx2, cy2),
                radius = w * 0.55f
            ),
            radius = w * 0.55f,
            center = Offset(cx2, cy2)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFEC4899).copy(a * 0.6f), Color.Transparent),
                center = Offset(w * 0.5f, h * (0.3f + 0.4f * p1)),
                radius = w * 0.5f
            ),
            radius = w * 0.5f,
            center = Offset(w * 0.5f, h * (0.3f + 0.4f * p1))
        )
    }
}

@Composable
fun NebulaEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "neb")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(24000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val r = Math.toRadians(rot.toDouble())
        val a = 0.7f * intensity

        val cx = w / 2f
        val cy = h / 2f

        fun orb(color: Color, ang: Double, dist: Float, rad: Float) {
            val x = cx + (cos(ang) * dist).toFloat()
            val y = cy + (sin(ang) * dist).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(color.copy(a), Color.Transparent),
                    center = Offset(x, y),
                    radius = rad
                ),
                radius = rad,
                center = Offset(x, y)
            )
        }

        orb(Color(0xFF8B5CF6), r,           w * 0.25f, w * 0.5f)
        orb(Color(0xFFEC4899), r + 2.1,     w * 0.3f,  w * 0.5f)
        orb(Color(0xFF06B6D4), r + 4.2,     w * 0.3f,  w * 0.5f)
        orb(Color(0xFFF59E0B), r + 5.6,     w * 0.35f, w * 0.4f)
    }
}

@Composable
fun PremiumAuroraEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "aurora")
    val shift by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Reverse),
        label = "shift"
    )
    val wave by t.animateFloat(
        -1f, 1f,
        infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Reverse),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.6f * intensity

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0A0A1F).copy(alpha = a * 0.4f),
                    Color(0xFF4C1D95).copy(alpha = a),
                    Color(0xFF831843).copy(alpha = a * 0.7f),
                    Color(0xFF0A0A1F).copy(alpha = a * 0.4f)
                ),
                startY = h * (0.2f + shift * 0.3f),
                endY = h * (0.9f + shift * 0.1f)
            )
        )

        val waveX = w * (0.3f + wave * 0.15f)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF10B981).copy(a * 0.7f), Color.Transparent),
                center = Offset(waveX, h * 0.15f),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(waveX, h * 0.15f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFA855F7).copy(a * 0.8f), Color.Transparent),
                center = Offset(w * 0.6f, h * 0.3f),
                radius = w * 0.75f
            ),
            radius = w * 0.75f,
            center = Offset(w * 0.6f, h * 0.3f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFEC4899).copy(a * 0.6f), Color.Transparent),
                center = Offset(w * 0.35f, h * 0.25f),
                radius = w * 0.6f
            ),
            radius = w * 0.6f,
            center = Offset(w * 0.35f, h * 0.25f)
        )
    }
}