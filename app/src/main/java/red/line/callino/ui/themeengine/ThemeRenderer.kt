package red.line.callino.ui.themeengine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.ui.effects.CallParticles
import red.line.callino.ui.effects.CinematicVignetteOverlay
import red.line.callino.ui.effects.GradientGlassOverlay

/**
 * CallThemeRenderer - Main Entry Point for rendering Incoming Call Themes.
 * Dispatches dynamically to:
 * - ImageThemeRenderer for IMAGE / Custom Photos
 * - VideoThemeRenderer for VIDEO / Custom Loops with ExoPlayer
 * - GradientThemeRenderer for GRADIENT / ANIMATION / Custom Shaders
 *
 * Layers visual effects:
 * - Dynamic blur
 * - Frosted Gradient & Dim Overlay
 * - Cinematic Vignette
 * - Floating Glowing Particles
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Base Content Layer (with optional blur)
        val contentModifier = if (enableBlur && blurAmount > 0f) {
            Modifier.fillMaxSize().blur(blurAmount.dp)
        } else {
            Modifier.fillMaxSize()
        }

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

        // Gradient Glass & Dim Overlay
        GradientGlassOverlay(
            dimAlpha = dimAlpha,
            modifier = Modifier.fillMaxSize()
        )

        // Cinematic Vignette Overlay
        CinematicVignetteOverlay(
            modifier = Modifier.fillMaxSize()
        )

        // Floating Glowing Particles Layer
        if (enableParticles && (settings?.animationsEnabled != false)) {
            CallParticles(
                modifier = Modifier.fillMaxSize(),
                particleCount = 28
            )
        }
    }
}

