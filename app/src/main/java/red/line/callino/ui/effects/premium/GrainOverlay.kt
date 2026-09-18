package red.line.callino.ui.effects.premium

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GrainOverlay(
    modifier: Modifier = Modifier,
    baseAlpha: Float = 0.06f
) {
    val t = rememberInfiniteTransition(label = "grain")
    val a by t.animateFloat(
        initialValue = baseAlpha * 0.5f,
        targetValue = baseAlpha * 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(160, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "grainAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(a)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color.Black,
                        Color.White,
                        Color.Black,
                        Color.White
                    ),
                    start = Offset.Zero,
                    end = Offset(120f, 120f)
                )
            )
    )
}