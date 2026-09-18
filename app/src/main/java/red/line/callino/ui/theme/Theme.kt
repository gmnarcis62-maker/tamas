package red.line.callino.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ============================================================
// 🌗 CALINO DESIGN SYSTEM — Theme Setup
// ============================================================

private val LightColorScheme = lightColorScheme(
    primary              = LightPalette.primary,
    onPrimary            = LightPalette.onPrimary,
    primaryContainer     = LightPalette.primaryContainer,
    onPrimaryContainer   = LightPalette.onPrimaryContainer,

    secondary            = LightPalette.secondary,
    onSecondary          = LightPalette.onSecondary,
    secondaryContainer   = LightPalette.secondaryContainer,

    tertiary             = LightPalette.accent,
    onTertiary           = LightPalette.onAccent,

    background           = LightPalette.background,
    onBackground         = LightPalette.textPrimary,

    surface              = LightPalette.surface,
    onSurface            = LightPalette.textPrimary,
    surfaceVariant       = LightPalette.surfaceVariant,
    onSurfaceVariant     = LightPalette.textSecondary,

    outline              = LightPalette.outline,
    outlineVariant       = LightPalette.outlineStrong,

    error                = LightPalette.error,
    onError              = Color.White,
    errorContainer       = LightPalette.errorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary              = DarkPalette.primary,
    onPrimary            = DarkPalette.onPrimary,
    primaryContainer     = DarkPalette.primaryContainer,
    onPrimaryContainer   = DarkPalette.onPrimaryContainer,

    secondary            = DarkPalette.secondary,
    onSecondary          = DarkPalette.onSecondary,
    secondaryContainer   = DarkPalette.secondaryContainer,

    tertiary             = DarkPalette.accent,
    onTertiary           = DarkPalette.onAccent,

    background           = DarkPalette.background,
    onBackground         = DarkPalette.textPrimary,

    surface              = DarkPalette.surface,
    onSurface            = DarkPalette.textPrimary,
    surfaceVariant       = DarkPalette.surfaceVariant,
    onSurfaceVariant     = DarkPalette.textSecondary,

    outline              = DarkPalette.outline,
    outlineVariant       = DarkPalette.outlineStrong,

    error                = DarkPalette.error,
    onError              = Color.White,
    errorContainer       = DarkPalette.errorContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,   // ← خاموش، چون پالت خودمون رو داریم
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CallinoTypography,
        content = content
    )
}

// ============================================================
// 🎨 Extension برای دسترسی راحت به پالت در سراسر برنامه
// ============================================================

/**
 * رنگ‌های سمانتیک که در MaterialTheme نیستن.
 * استفاده:
 *   val myColors = LocalCallinoColors.current
 *   Text(color = myColors.success)
 */
data class CallinoColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val accent: Color,
    val onAccent: Color,
    val accentContainer: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val surfaceElevated: Color,
    val gradientPrimary: List<Color>,
    val gradientAccent: List<Color>
)

val LightCallinoColors = CallinoColors(
    success = LightPalette.success,
    onSuccess = Color.White,
    successContainer = Color(0xFFD1FAE5),
    accent = LightPalette.accent,
    onAccent = LightPalette.onAccent,
    accentContainer = LightPalette.accentContainer,
    textPrimary = LightPalette.textPrimary,
    textSecondary = LightPalette.textSecondary,
    textTertiary = LightPalette.textTertiary,
    surfaceElevated = LightPalette.surfaceElevated,
    gradientPrimary = listOf(Indigo500, Violet500),
    gradientAccent = listOf(Gold400, Gold600)
)

val DarkCallinoColors = CallinoColors(
    success = DarkPalette.success,
    onSuccess = Color.White,
    successContainer = Color(0xFF064E3B),
    accent = DarkPalette.accent,
    onAccent = DarkPalette.onAccent,
    accentContainer = DarkPalette.accentContainer,
    textPrimary = DarkPalette.textPrimary,
    textSecondary = DarkPalette.textSecondary,
    textTertiary = DarkPalette.textTertiary,
    surfaceElevated = DarkPalette.surfaceElevated,
    gradientPrimary = listOf(Indigo400, Violet400),
    gradientAccent = listOf(Gold400, Gold500)
)

val LocalCallinoColors = androidx.compose.runtime.staticCompositionLocalOf { LightCallinoColors }

@Composable
fun CallinoThemeProvider(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkCallinoColors else LightCallinoColors
    androidx.compose.runtime.CompositionLocalProvider(
        LocalCallinoColors provides colors
    ) {
        MyApplicationTheme(darkTheme = darkTheme, content = content)
    }
}