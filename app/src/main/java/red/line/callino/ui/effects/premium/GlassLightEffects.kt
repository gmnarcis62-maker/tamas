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
fun LiquidGlassEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "liquid")
    val p by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.7f * intensity

        val cx = w * (0.4f + 0.2f * p)
        val cy = h * (0.4f + 0.2f * p)

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF8B5CF6).copy(a), Color.Transparent),
                center = Offset(cx, cy),
                radius = w * 0.7f
            ),
            radius = w * 0.7f, center = Offset(cx, cy)
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFEC4899).copy(a * 0.7f), Color.Transparent),
                center = Offset(w - cx, h - cy),
                radius = w * 0.7f
            ),
            radius = w * 0.7f, center = Offset(w - cx, h - cy)
        )
    }
}

@Composable
fun GlassRefractionEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "refrac")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.6f * intensity

        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF8B5CF6).copy(a),
                    Color(0xFFEC4899).copy(a),
                    Color(0xFFF59E0B).copy(a),
                    Color(0xFF10B981).copy(a),
                    Color(0xFF06B6D4).copy(a),
                    Color(0xFF8B5CF6).copy(a)
                ),
                center = Offset(w / 2f, h / 2f)
            ),
            alpha = 0.85f
        )
    }
}

@Composable
fun GlassMorphEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "morph")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(25000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )
    val pulse by t.animateFloat(
        0.9f, 1.1f,
        infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.55f * intensity

        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFA855F7).copy(a),
                    Color(0xFFEC4899).copy(a),
                    Color(0xFF06B6D4).copy(a),
                    Color(0xFFA855F7).copy(a)
                ),
                center = Offset(w / 2f, h / 2f)
            ),
            alpha = 0.8f
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color.White.copy(a * 0.15f * pulse), Color.Transparent),
                center = Offset(w / 2f, h / 2f),
                radius = w * 0.6f
            ),
            radius = w * 0.6f, center = Offset(w / 2f, h / 2f)
        )
    }
}

@Composable
fun VolumetricBeamsEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "volum")
    val sway by t.animateFloat(
        -1f, 1f,
        infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse),
        label = "sway"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.5f * intensity

        for (i in 0..4) {
            val baseX = w * (0.1f + i * 0.2f)
            val xOffset = sway * w * 0.05f * (i - 2)
            val x = baseX + xOffset

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFA855F7).copy(a * 0.9f),
                        Color(0xFFEC4899).copy(a * 0.5f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = h
                ),
                topLeft = Offset(x - 30f, 0f),
                size = androidx.compose.ui.geometry.Size(60f, h * 0.85f)
            )
        }
    }
}

@Composable
fun BloomEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "bloom")
    val pulse by t.animateFloat(
        0.7f, 1.4f,
        infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.75f * intensity
        val cx = w / 2f
        val cy = h * 0.4f

        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Color.White.copy(a * 0.5f),
                    Color(0xFFF0ABFC).copy(a * 0.7f),
                    Color(0xFFA855F7).copy(a * 0.4f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = w * 0.5f * pulse
            ),
            radius = w * 0.5f * pulse,
            center = Offset(cx, cy)
        )
    }
}

@Composable
fun PhotonBeamsEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "photon")
    val sway by t.animateFloat(
        -1f, 1f,
        infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "sw"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.55f * intensity

        for (i in 0..6) {
            val baseX = w * 0.5f + (i - 3) * w * 0.1f
            val x = baseX + sway * 30f * (i - 3) * 0.3f

            drawLine(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFA855F7).copy(a),
                        Color(0xFFEC4899).copy(a * 0.5f),
                        Color.Transparent
                    ),
                    startY = h,
                    endY = 0f
                ),
                start = Offset(x, h),
                end = Offset(x + sway * 40f, 0f),
                strokeWidth = 8f
            )
        }
    }
}

@Composable
fun PrismLightEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "prism")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.55f * intensity

        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0xFFFF0050).copy(a),
                    Color(0xFFFFB400).copy(a),
                    Color(0xFF00FF64).copy(a),
                    Color(0xFF00B4FF).copy(a),
                    Color(0xFF7800FF).copy(a),
                    Color.Transparent
                ),
                center = Offset(w / 2f, h / 2f)
            ),
            alpha = 0.75f
        )
    }
}

@Composable
fun NeonPulseEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "neonpulse")
    val p by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.7f * intensity
        val cx = w / 2f
        val cy = h * 0.45f
        val baseRadius = w * 0.15f
        val radius = baseRadius * (0.6f + p * 2.2f)
        val alpha = a * (1f - p)

        drawCircle(
            color = Color(0xFFA855F7).copy(alpha),
            radius = radius,
            center = Offset(cx, cy),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )
        drawCircle(
            color = Color(0xFFEC4899).copy(alpha * 0.7f),
            radius = radius * 1.15f,
            center = Offset(cx, cy),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
        )
    }
}

@Composable
fun EnergyAuraEffect(intensity: Float = 1f) {
    val t = rememberInfiniteTransition(label = "aura")
    val pulse by t.animateFloat(
        0.85f, 1.15f,
        infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "p"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val a = 0.75f * intensity
        val cx = w / 2f
        val cy = h * 0.22f

        drawCircle(
            brush = Brush.radialGradient(
                listOf(
                    Color(0xFFA855F7).copy(a),
                    Color(0xFFEC4899).copy(a * 0.4f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = w * 0.35f * pulse
            ),
            radius = w * 0.35f * pulse,
            center = Offset(cx, cy)
        )
    }
}