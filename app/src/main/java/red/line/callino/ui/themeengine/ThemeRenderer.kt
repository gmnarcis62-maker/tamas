package red.line.callino.ui.themeengine

import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.data.EffectType
import red.line.callino.data.ThemeType
import red.line.callino.ui.effects.CallParticles
import red.line.callino.ui.effects.CinematicVignetteOverlay
import red.line.callino.ui.effects.GradientGlassOverlay
import kotlin.math.sin

/**
 * CallThemeRenderer - نقطه ورود اصلی برای رندر تم‌های صفحه تماس
 * شامل پیاده‌سازی ۱۵ افکت مختلف روی محتوای تم
 */
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
        // ✅ لایه محتوا + افکت اختصاصی
        val contentModifier = buildContentModifier(
            effect = theme.effect,
            animationsEnabled = animationsEnabled,
            enableBlur = enableBlur,
            blurAmount = blurAmount
        )

        Box(modifier = contentModifier) {
            when (theme.type) {
                ThemeType.VIDEO -> {
                    VideoThemeRenderer(theme = theme)
                }
                ThemeType.IMAGE -> {
                    ImageThemeRenderer(theme = theme)
                }
                ThemeType.ANIMATION, ThemeType.CUSTOM -> {
                    if (!theme.mediaUri.isNullOrBlank()) {
                        ImageThemeRenderer(theme = theme)
                    } else {
                        GradientThemeRenderer(theme = theme)
                    }
                }
            }
        }

        // گرادیانت و تاری
        GradientGlassOverlay(
            dimAlpha = dimAlpha,
            modifier = Modifier.fillMaxSize()
        )

        // Vignette سینمایی
        CinematicVignetteOverlay(
            modifier = Modifier.fillMaxSize()
        )

        // افکت‌های اضافی که روی overlay قرار می‌گیرن
        EffectOverlayLayer(
            effect = theme.effect,
            animationsEnabled = animationsEnabled
        )

        // ذرات شناور
        if (enableParticles && animationsEnabled) {
            CallParticles(
                modifier = Modifier.fillMaxSize(),
                particleCount = if (theme.effect == EffectType.PARTICLES) 45 else 28
            )
        }
    }
}

/**
 * ساخت Modifier محتوا بر اساس افکت
 */
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
            val alpha by transition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha"
            )
            base.alpha(alpha)
        }

        EffectType.SCALE_BREATHE -> {
            val scale by transition.animateFloat(
                initialValue = 1f,
                targetValue = 1.06f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )
            base.scale(scale)
        }

        EffectType.ZOOM_IN_OUT -> {
            val scale by transition.animateFloat(
                initialValue = 1f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(7000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "zoom"
            )
            base.scale(scale)
        }

        EffectType.ROTATE_SLOW -> {
            val rotation by transition.animateFloat(
                initialValue = -3f,
                targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(8000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "rotate"
            )
            base.rotate(rotation)
        }

        EffectType.PULSE_GLOW -> {
            val scale by transition.animateFloat(
                initialValue = 1f,
                targetValue = 1.04f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse"
            )
            val alpha by transition.animateFloat(
                initialValue = 0.92f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )
            base.scale(scale).alpha(alpha)
        }

        EffectType.COLOR_SHIFT -> {
            val hue by transition.animateFloat(
                initialValue = -25f,
                targetValue = 25f,
                animationSpec = infiniteRepeatable(
                    animation = tween(6000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "hue"
            )
            val matrix = ColorMatrix().apply { setToSaturation(1f) }
            base.graphicsLayer {
                renderEffect = null
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix(floatArrayOf(
                        cosRad(hue), sinRad(hue), 0f, 0f, 0f,
                        -sinRad(hue), cosRad(hue), 0f, 0f, 0f,
                        0f, 0f, 1f, 0f, 0f,
                        0f, 0f, 0f, 1f, 0f
                    ))
                )
            }
        }

        EffectType.BLUR_PULSE -> {
            val blur by transition.animateFloat(
                initialValue = 0f,
                targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "blurPulse"
            )
            base.blur(blur.dp)
        }

        EffectType.SHAKE_SOFT -> {
            val offsetX by transition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "shakeX"
            )
            val offsetY by transition.animateFloat(
                initialValue = -4f,
                targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(450, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "shakeY"
            )
            base.graphicsLayer {
                translationX = offsetX
                translationY = offsetY
            }
        }

        EffectType.FLIP_HORIZONTAL -> {
            val scaleX by transition.animateFloat(
                initialValue = 1f,
                targetValue = -1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "flip"
            )
            base.graphicsLayer {
                scaleX = if (scaleX < -0.5f) -1f else 1f
            }
        }

        EffectType.WAVE -> {
            val offsetY by transition.animateFloat(
                initialValue = -12f,
                targetValue = 12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "waveY"
            )
            base.graphicsLayer {
                translationY = offsetY
            }
        }

        EffectType.GRADIENT_SHIFT -> {
            val scale by transition.animateFloat(
                initialValue = 1.02f,
                targetValue = 1.12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "gradShift"
            )
            base.scale(scale).rotate(2f)
        }

        EffectType.NEON_BORDER,
        EffectType.FILM_GRAIN,
        EffectType.PARTICLES,
        EffectType.NONE -> {
            if (enableBlur && blurAmount > 0f) {
                base.blur(blurAmount.dp)
            } else base
        }
    }
}

/**
 * لایه‌های افکت روی overlay که روی محتوا قرار می‌گیرن
 * (نئون بردر، گرین فیلم و...)
 */
@Composable
private fun EffectOverlayLayer(
    effect: EffectType,
    animationsEnabled: Boolean
) {
    if (!animationsEnabled) return

    when (effect) {
        EffectType.NEON_BORDER -> {
            NeonBorderOverlay()
        }
        EffectType.FILM_GRAIN -> {
            FilmGrainOverlay()
        }
        else -> { /* بدون overlay */ }
    }
}

@Composable
private fun NeonBorderOverlay() {
    val transition = rememberInfiniteTransition(label = "neon")
    val alpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neonAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                // حاشیه نئون با ترکیب گرادیانت
            }
    ) {
        // چهار نوار نئون در لبه‌ها
        NeonEdge(alpha = alpha)
    }
}

@Composable
private fun NeonEdge(alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF06B6D4).copy(alpha = 0.35f * alpha),
                        Color(0xFFEC4899).copy(alpha = 0.5f * alpha)
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
    val alpha by transition.animateFloat(
        initialValue = 0.03f,
        targetValue = 0.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(180, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "grainAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White, Color.Black, Color.White, Color.Black
                    )
                )
            )
    )
}

// ---------- Helpers ----------

private fun cosRad(deg: Float): Float = kotlin.math.cos(Math.toRadians(deg.toDouble())).toFloat()
private fun sinRad(deg: Float): Float = kotlin.math.sin(Math.toRadians(deg.toDouble())).toFloat()