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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun HolographicFoilEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "holo")
    val shift by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(5500, easing = LinearEasing), RepeatMode.Restart),
        label = "sh"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.55f * intensity

        val offsetX = -w + shift * w * 2f

        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1E1B4B).copy(a * 0.4f),
                    Color(0xFF6366F1).copy(a),
                    Color(0xFFA855F7).copy(a),
                    Color(0xFFEC4899).copy(a),
                    Color(0xFFF59E0B).copy(a),
                    Color(0xFF10B981).copy(a),
                    Color(0xFF06B6D4).copy(a),
                    Color(0xFF1E1B4B).copy(a * 0.4f)
                ),
                start = Offset(offsetX, 0f),
                end = Offset(offsetX + w * 2f, h)
            )
        )
    }
}

@Composable
fun ChromeMetalEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "chrome")
    val shift by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse),
        label = "sh"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.6f * intensity

        val dx = (shift - 0.5f) * w

        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0A0A0A).copy(a * 0.5f),
                    Color(0xFF404040).copy(a),
                    Color(0xFFD4D4D8).copy(a),
                    Color(0xFF525252).copy(a),
                    Color(0xFFE4E4E7).copy(a),
                    Color(0xFF262626).copy(a),
                    Color(0xFF0A0A0A).copy(a * 0.5f)
                ),
                start = Offset(-w + dx * 2f, 0f),
                end = Offset(w + dx * 2f, h)
            )
        )
    }
}

@Composable
fun LiquidMetalEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "liqMetal")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.6f * intensity

        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF1A1A2E).copy(a),
                    Color(0xFF6B7280).copy(a),
                    Color(0xFFE5E7EB).copy(a),
                    Color(0xFF4B5563).copy(a),
                    Color(0xFF9CA3AF).copy(a),
                    Color(0xFF1A1A2E).copy(a)
                ),
                center = Offset(w / 2f, h / 2f)
            )
        )
    }
}

@Composable
fun CrystalPrismEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "crystal")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(20000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.55f * intensity
        val r = Math.toRadians(rot.toDouble())

        fun corner(color: Color, ang: Double, fromX: Float, fromY: Float) {
            val x = fromX + (cos(ang) * w * 0.3f).toFloat()
            val y = fromY + (sin(ang) * h * 0.3f).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(color.copy(a), Color.Transparent),
                    center = Offset(x, y),
                    radius = w * 0.5f
                ),
                radius = w * 0.5f,
                center = Offset(x, y)
            )
        }

        corner(Color(0xFF8B5CF6), r,           w * 0.3f, h * 0.3f)
        corner(Color(0xFFEC4899), r + 2.1,     w * 0.7f, h * 0.3f)
        corner(Color(0xFF06B6D4), r + 4.2,     w * 0.5f, h * 0.7f)
        corner(Color(0xFFF59E0B), r + 1.05,    w * 0.2f, h * 0.8f)
    }
}

@Composable
fun ConstellationEffect(intensity: Float = 1f) {
    val stars = remember {
        val rnd = Random(42)
        List(30) { Offset(rnd.nextFloat(), rnd.nextFloat()) }
    }

    val t = rememberInfiniteTransition(label = "const")
    val pulse by t.animateFloat(
        0.4f, 1f,
        infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.6f * intensity

        for (i in stars.indices) {
            for (j in i + 1 until stars.size) {
                val ax = stars[i].x * w
                val ay = stars[i].y * h
                val bx = stars[j].x * w
                val by = stars[j].y * h
                val d = kotlin.math.hypot(ax - bx, ay - by)
                if (d < w * 0.32f) {
                    val lineAlpha = a * 0.4f * (1f - d / (w * 0.32f)) * pulse
                    drawLine(
                        color = Color(0xFFA855F7).copy(lineAlpha),
                        start = Offset(ax, ay),
                        end = Offset(bx, by),
                        strokeWidth = 1.5f
                    )
                }
            }
        }

        stars.forEachIndexed { idx, s ->
            val starPulse = 0.4f + 0.6f * (((idx * 13) % 100) / 100f) * pulse
            drawCircle(
                color = Color.White.copy(a * starPulse),
                radius = 3f,
                center = Offset(s.x * w, s.y * h)
            )
            drawCircle(
                color = Color(0xFFA855F7).copy(a * 0.4f * starPulse),
                radius = 6f,
                center = Offset(s.x * w, s.y * h)
            )
        }
    }
}

@Composable
fun CosmicDustEffect(intensity: Float = 1f) {
    val dusts = remember {
        val rnd = Random(7)
        List(60) {
            DustParticle(
                x = rnd.nextFloat(),
                y = rnd.nextFloat(),
                size = 1f + rnd.nextFloat() * 3f,
                speed = 0.3f + rnd.nextFloat() * 0.7f,
                phase = rnd.nextFloat() * 6.28f,
                color = listOf(
                    Color.White, Color(0xFFC4B5FD), Color(0xFFF0ABFC), Color(0xFF67E8F9)
                )[rnd.nextInt(4)]
            )
        }
    }

    val t = rememberInfiniteTransition(label = "dust")
    val time by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Restart),
        label = "t"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.8f * intensity

        dusts.forEach { d ->
            val drift = (time + d.phase) % 1f
            val cx = d.x * w + sin(drift * 6.28f) * 30f
            val cy = (d.y + drift * d.speed * 0.5f) % 1f * h

            drawCircle(
                color = d.color.copy(a),
                radius = d.size,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = d.color.copy(a * 0.3f),
                radius = d.size * 3f,
                center = Offset(cx, cy)
            )
        }
    }
}

private data class DustParticle(
    val x: Float, val y: Float, val size: Float,
    val speed: Float, val phase: Float, val color: Color
)

@Composable
fun CausticsEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "caustics")
    val p1 by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p1"
    )
    val p2 by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p2"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.55f * intensity

        val cx1 = w * (0.3f + p1 * 0.4f)
        val cy1 = h * (0.3f + p2 * 0.4f)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF06B6D4).copy(a), Color.Transparent),
                center = Offset(cx1, cy1),
                radius = w * 0.55f
            ),
            radius = w * 0.55f, center = Offset(cx1, cy1)
        )

        val cx2 = w * (0.7f - p2 * 0.4f)
        val cy2 = h * (0.6f - p1 * 0.4f)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF67E8F9).copy(a * 0.7f), Color.Transparent),
                center = Offset(cx2, cy2),
                radius = w * 0.5f
            ),
            radius = w * 0.5f, center = Offset(cx2, cy2)
        )
    }
}

@Composable
fun ChromaticAberrationEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "chroma")
    val shift by t.animateFloat(
        -1f, 1f,
        infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "sh"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.5f * intensity
        val dx = shift * 8f

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFFF0050).copy(a), Color.Transparent),
                center = Offset(w * 0.45f + dx, h * 0.5f),
                radius = w * 0.5f
            ),
            radius = w * 0.5f, center = Offset(w * 0.45f + dx, h * 0.5f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF00B4FF).copy(a), Color.Transparent),
                center = Offset(w * 0.55f - dx, h * 0.5f),
                radius = w * 0.5f
            ),
            radius = w * 0.5f, center = Offset(w * 0.55f - dx, h * 0.5f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF00FF96).copy(a * 0.6f), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.55f),
                radius = w * 0.4f
            ),
            radius = w * 0.4f, center = Offset(w * 0.5f, h * 0.55f)
        )
    }
}