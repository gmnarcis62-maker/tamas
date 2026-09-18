package red.line.callino.ui.themeengine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import red.line.callino.AssetImage
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType

/**
 * ImageThemeRenderer - Renders custom user images or preset static image themes.
 *
 * برای تم‌های IMAGE و ANIMATION از AssetImage استفاده می‌کنیم تا Coil
 * مستقیم از AssetManager لود کنه. برای CUSTOM از URI کاربر استفاده می‌شه.
 */
@Composable
fun ImageThemeRenderer(
    theme: CallTheme,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uri = theme.mediaUri

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (!uri.isNullOrBlank()) {
            val data: Any = when (theme.type) {
                ThemeType.IMAGE, ThemeType.ANIMATION -> AssetImage(uri)
                ThemeType.VIDEO -> uri
                ThemeType.CUSTOM -> uri
            }
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(data)
                    .crossfade(true)
                    .build(),
                contentDescription = theme.titleFa,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Preset static canvas if no media URI
            GradientThemeRenderer(theme = theme, modifier = Modifier.fillMaxSize())
        }
    }
}