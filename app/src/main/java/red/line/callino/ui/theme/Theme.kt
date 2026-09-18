package red.line.callino.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = FrostedPrimary,
    onPrimary = Color.White,
    primaryContainer = FrostedTintDeep,
    onPrimaryContainer = Color(0xFF21005D),
    secondary = FrostedSecondary,
    onSecondary = Color.White,
    secondaryContainer = FrostedTint,
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = FrostedTertiary,
    onTertiary = Color(0xFF381E72),
    tertiaryContainer = FrostedTintLight,
    onTertiaryContainer = Color(0xFF21005D),
    background = FrostedBg,
    onBackground = FrostedTextPrimary,
    surface = FrostedGlassSolid,
    onSurface = FrostedTextPrimary,
    surfaceVariant = FrostedContainer,
    onSurfaceVariant = FrostedTextSecondary,
    outline = FrostedBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color(0xFFF3F4F9).toArgb()
                window.navigationBarColor = Color(0xFFFFFFFF).toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
