package red.line.callino.ui.effects

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * GlowEffect - Radiant pulsing colored halo for call actions.
 */
@Composable
fun GlowEffect(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0xFF22C55E),
    radiusDp: Dp = 80.dp,
    pulsing: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow_pulse")
    val alphaMultiplier by if (pulsing) {
        infiniteTransition.animateFloat(
            initialValue = 0.55f,
            targetValue = 0.95f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glow_alpha"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0.75f) }
    }

    Box(
        modifier = modifier.size(radiusDp * 2),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.minDimension / 2f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.7f * alphaMultiplier),
                        glowColor.copy(alpha = 0.35f * alphaMultiplier),
                        glowColor.copy(alpha = 0.1f * alphaMultiplier),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxRadius
                ),
                radius = maxRadius,
                center = center
            )
        }
    }
}
