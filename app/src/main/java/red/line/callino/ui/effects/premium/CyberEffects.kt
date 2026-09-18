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
fun GlitchNeonEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "glitch")
    val shift by t.animateFloat(
        -1f, 1f,
        infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Reverse),
        label = "sh"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.5f * intensity
        val dx = shift * 6f

        for (i in 0..12) {
            val y = h * (i / 13f)
            val redOff = if (i % 3 == 0) dx else -dx
            val blueOff = if (i % 3 == 1) dx else -dx

            drawLine(
                color = Color(0xFFFF0050).copy(a),
                start = Offset(0f + redOff, y),
                end = Offset(w + redOff, y),
                strokeWidth = 2f
            )
            drawLine(
                color = Color(0xFF00B4FF).copy(a),
                start = Offset(0f + blueOff, y + 3f),
                end = Offset(w + blueOff, y + 3f),
                strokeWidth = 2f
            )
        }

        drawRect(
            brush = Brush.radialGradient(
                listOf(Color(0xFFEC4899).copy(a * 0.4f), Color.Transparent),
                center = Offset(w / 2f, h / 2f),
                radius = w * 0.7f
            )
        )
    }
}

@Composable
fun SonicWaveEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "sonic")
    val p by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.65f * intensity
        val cx = w / 2f
        val cy = h / 2f
        val baseR = w * 0.15f

        for (i in 0..2) {
            val phase = (p + i * 0.33f) % 1f
            val radius = baseR + phase * w * 0.7f
            val alpha = a * (1f - phase)

            drawCircle(
                color = Color(0xFF06B6D4).copy(alpha),
                radius = radius,
                center = Offset(cx, cy),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
            )
            drawCircle(
                color = Color(0xFFA855F7).copy(alpha * 0.6f),
                radius = radius + 5f,
                center = Offset(cx, cy),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }
    }
}

@Composable
fun DepthParallaxEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "parallax")
    val time by t.animateFloat(
        0f, 6.28f,
        infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Restart),
        label = "t"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.6f * intensity

        val x1 = w * 0.3f + cos(time * 0.5f) * 20f
        val y1 = h * 0.3f + sin(time * 0.5f) * 20f
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF8B5CF6).copy(a), Color.Transparent),
                center = Offset(x1, y1),
                radius = w * 0.7f
            ),
            radius = w * 0.7f, center = Offset(x1, y1)
        )

        val x2 = w * 0.7f + sin(time * 0.8f) * 40f
        val y2 = h * 0.6f + cos(time * 0.8f) * 40f
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFEC4899).copy(a * 0.8f), Color.Transparent),
                center = Offset(x2, y2),
                radius = w * 0.65f
            ),
            radius = w * 0.65f, center = Offset(x2, y2)
        )

        val x3 = w * 0.5f + sin(time * 1.2f) * 70f
        val y3 = h * 0.5f + cos(time * 1.2f) * 70f
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF06B6D4).copy(a * 0.6f), Color.Transparent),
                center = Offset(x3, y3),
                radius = w * 0.5f
            ),
            radius = w * 0.5f, center = Offset(x3, y3)
        )
    }
}