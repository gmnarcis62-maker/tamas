package red.line.callino.ui.effects

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
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    val initialX: Float,
    val initialY: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float,
    val horizontalWiggle: Float,
    val color: Color
)

/**
 * CallParticles - High performance floating glowing micro-particles for Incoming Call Screen.
 */
@Composable
fun CallParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 30,
    particleColor: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles_movement")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )

    // Pre-calculate deterministic particles
    val particles = remember(particleCount, particleColor) {
        val random = Random(42)
        List(particleCount) {
            val isGold = random.nextBoolean()
            val color = if (isGold) Color(0xFFFDE047) else particleColor
            Particle(
                initialX = random.nextFloat(),
                initialY = random.nextFloat(),
                radius = random.nextFloat() * 3.5f + 1.5f,
                speed = random.nextFloat() * 0.4f + 0.6f,
                alpha = random.nextFloat() * 0.5f + 0.2f,
                horizontalWiggle = (random.nextFloat() - 0.5f) * 40f,
                color = color
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        for (p in particles) {
            // Particles move upwards smoothly
            val currentYFraction = (p.initialY - (progress * p.speed)) % 1f
            val adjustedY = (if (currentYFraction < 0) currentYFraction + 1f else currentYFraction) * canvasHeight
            val wiggleX = sin((progress * Math.PI * 4 + p.initialX * 10).toDouble()).toFloat() * p.horizontalWiggle
            val currentX = (p.initialX * canvasWidth + wiggleX).coerceIn(0f, canvasWidth)

            // Draw glowing halo around particle
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        p.color.copy(alpha = p.alpha),
                        p.color.copy(alpha = p.alpha * 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(currentX, adjustedY),
                    radius = p.radius * 3.5f
                ),
                radius = p.radius * 3.5f,
                center = Offset(currentX, adjustedY)
            )

            // Draw solid core
            drawCircle(
                color = Color.White.copy(alpha = (p.alpha * 1.5f).coerceAtMost(1f)),
                radius = p.radius,
                center = Offset(currentX, adjustedY)
            )
        }
    }
}
