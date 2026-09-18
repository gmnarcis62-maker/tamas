package red.line.callino.ui.themeengine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.data.EffectRandomizer
import red.line.callino.data.EffectType
import red.line.callino.data.ThemeType
import red.line.callino.ui.effects.CinematicVignetteOverlay
import red.line.callino.ui.effects.GradientGlassOverlay
import red.line.callino.ui.effects.premium.PremiumEffectRenderer

@Composable
fun CallThemeRenderer(
    theme: CallTheme,
    settings: AppSettings? = null,
    dimAlpha: Float = settings?.backgroundDim ?: 0.3f,
    modifier: Modifier = Modifier
) {
    val enableBlur = settings?.enableBlur ?: false
    val blurAmount = settings?.blurAmount ?: 0f
    val animationsEnabled = settings?.animationsEnabled ?: true

    val resolvedEffect: EffectType = remember(theme.id, settings) {
        if (settings == null) theme.effect
        else EffectRandomizer.resolve(
            themeId = theme.id,
            defaultEffect = theme.effect,
            settings = settings
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val contentModifier = if (enableBlur && blurAmount > 0f) {
            Modifier.fillMaxSize().blur(blurAmount.dp)
        } else {
            Modifier.fillMaxSize()
        }

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

        PremiumEffectRenderer(
            effect = resolvedEffect,
            animationsEnabled = animationsEnabled,
            modifier = Modifier.fillMaxSize()
        )

        CinematicVignetteOverlay(
            modifier = Modifier.fillMaxSize()
        )
    }
}