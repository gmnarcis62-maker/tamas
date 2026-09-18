package red.line.callino.ui.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Concentric multi-stage pulsing rings effect.
 */
@Composable
fun ConcentricPulseRings(
    modifier: Modifier = Modifier,
    ringColor: Color = Color(0xFF22C55E),
    maxRadiusDp: Dp = 100.dp,
    ringCount: Int = 3
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")

    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_progress"
    )

    Box(
        modifier = modifier.size(maxRadiusDp * 2),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadiusPx = size.minDimension / 2f

            for (i in 0 until ringCount) {
                val offsetFraction = i.toFloat() / ringCount
                val currentProgress = (pulseProgress + offsetFraction) % 1f
                val ringRadius = maxRadiusPx * currentProgress
                val alpha = (1f - currentProgress).coerceIn(0f, 1f) * 0.75f

                drawCircle(
                    color = ringColor.copy(alpha = alpha),
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = 2.5f.dp.toPx() * (1f - currentProgress * 0.5f))
                )
            }
        }
    }
}
