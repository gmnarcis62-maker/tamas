package red.line.callino.ui.themeengine

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.data.EffectType
import red.line.callino.data.ThemeType
import red.line.callino.ui.effects.CallParticles
import red.line.callino.ui.effects.CinematicVignetteOverlay
import red.line.callino.ui.effects.GradientGlassOverlay

@Composable
fun CallThemeRenderer(
    theme: CallTheme,
    settings: AppSettings? = null,
    dimAlpha: Float = settings?.backgroundDim ?: 0.3f,
    modifier: Modifier = Modifier
) {
    val enableBlur = settings?.enableBlur ?: false
    val blurAmount = settings?.blurAmount ?: 0f
    val enableParticles = settings?.enableParticles ?: true
    val animationsEnabled = settings?.animationsEnabled ?: true

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val contentModifier = buildContentModifier(
            effect = theme.effect,
            animationsEnabled = animationsEnabled,
            enableBlur = enableBlur,
            blurAmount = blurAmount
        )

        Box(modifier = contentModifier) {
            when (theme.type) {
                ThemeType.VIDEO -> VideoThemeRenderer(theme = theme)
                ThemeType.IMAGE -> ImageThemeRenderer(theme = theme)
                ThemeType.ANIMATION, ThemeType.CUSTOM -> {
                    if (!theme.mediaUri.isNullOrBlank()) {
                        ImageThemeRenderer(theme = theme)
                    } else {
                        GradientThemeRenderer(theme = theme)
                    }
                }
            }
        }

        GradientGlassOverlay(
            dimAlpha = dimAlpha,
            modifier = Modifier.fillMaxSize()
        )

        CinematicVignetteOverlay(
            modifier = Modifier.fillMaxSize()
        )

        EffectOverlayLayer(
            effect = theme.effect,
            animationsEnabled = animationsEnabled
        )

        if (enableParticles && animationsEnabled) {
            CallParticles(
                modifier = Modifier.fillMaxSize(),
                particleCount = if (theme.effect == EffectType.PARTICLES) 45 else 28
            )
        }
    }
}

@Composable
private fun buildContentModifier(
    effect: EffectType,
    animationsEnabled: Boolean,
    enableBlur: Boolean,
    blurAmount: Float
): Modifier {
    val base = Modifier.fillMaxSize()

    if (!animationsEnabled) {
        return if (enableBlur && blurAmount > 0f) base.blur(blurAmount.dp) else base
    }

    val transition = rememberInfiniteTransition(label = "effect_${effect.id}")

    return when (effect) {
        EffectType.FADE_LOOP -> {
            val alphaAnim by transition.animateFloat(
                initialValue = 0.4f, targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "alpha"
            )
            base.alpha(alphaAnim)
        }

        EffectType.SCALE_BREATHE -> {
            val scaleAnim by transition.animateFloat(
                initialValue = 1f, targetValue = 1.06f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "scale"
            )
            base.scale(scaleAnim)
        }

        EffectType.ZOOM_IN_OUT -> {
            val scaleAnim by transition.animateFloat(
                initialValue = 1f, targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(7000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "zoom"
            )
            base.scale(scaleAnim)
        }

        EffectType.ROTATE_SLOW -> {
            val rotAnim by transition.animateFloat(
                initialValue = -3f, targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(8000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "rotate"
            )
            base.rotate(rotAnim)
        }

        EffectType.PULSE_GLOW -> {
            val scaleAnim by transition.animateFloat(
                initialValue = 1f, targetValue = 1.04f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "pulse"
            )
            val alphaAnim by transition.animateFloat(
                initialValue = 0.92f, targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "pulseAlpha"
            )
            base.scale(scaleAnim).alpha(alphaAnim)
        }

        EffectType.COLOR_SHIFT -> {
            val scaleAnim by transition.animateFloat(
                initialValue = 0.98f, targetValue = 1.04f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "colorShift"
            )
            base.scale(scaleAnim).rotate(1.5f)
        }

        EffectType.BLUR_PULSE -> {
            val blurAnim by transition.animateFloat(
                initialValue = 0f, targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "blurPulse"
            )
            base.blur(blurAnim.dp)
        }

        EffectType.SHAKE_SOFT -> {
            val offsetX by transition.animateFloat(
                initialValue = -6f, targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "shakeX"
            )
            val offsetY by transition.animateFloat(
                initialValue = -4f, targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(450, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "shakeY"
            )
            base.graphicsLayer {
                translationX = offsetX
                translationY = offsetY
            }
        }

        EffectType.FLIP_HORIZONTAL -> {
            val flipValue by transition.animateFloat(
                initialValue = 0f, targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "flip"
            )
            base.graphicsLayer {
                scaleX = if (flipValue < 0.5f) 1f else -1f
            }
        }

        EffectType.WAVE -> {
            val offsetY by transition.animateFloat(
                initialValue = -12f, targetValue = 12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "waveY"
            )
            base.graphicsLayer { translationY = offsetY }
        }

        EffectType.GRADIENT_SHIFT -> {
            val scaleAnim by transition.animateFloat(
                initialValue = 1.02f, targetValue = 1.12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "gradShift"
            )
            base.scale(scaleAnim).rotate(2f)
        }

        EffectType.NEON_BORDER,
        EffectType.FILM_GRAIN,
        EffectType.PARTICLES,
        EffectType.NONE -> {
            if (enableBlur && blurAmount > 0f) base.blur(blurAmount.dp) else base
        }
    }
}

@Composable
private fun EffectOverlayLayer(
    effect: EffectType,
    animationsEnabled: Boolean
) {
    if (!animationsEnabled) return

    when (effect) {
        EffectType.NEON_BORDER -> NeonBorderOverlay()
        EffectType.FILM_GRAIN -> FilmGrainOverlay()
        else -> Unit
    }
}

@Composable
private fun NeonBorderOverlay() {
    val transition = rememberInfiniteTransition(label = "neon")
    val alphaAnim by transition.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "neonAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF06B6D4).copy(alpha = 0.25f * alphaAnim),
                        Color(0xFFEC4899).copy(alpha = 0.4f * alphaAnim)
                    ),
                    center = Offset.Unspecified,
                    radius = 1200f
                )
            )
    )
}

@Composable
private fun FilmGrainOverlay() {
    val transition = rememberInfiniteTransition(label = "grain")
    val alphaAnim by transition.animateFloat(
        initialValue = 0.03f, targetValue = 0.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(180, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "grainAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alphaAnim)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color.White, Color.Black, Color.White, Color.Black)
                )
            )
    )
}