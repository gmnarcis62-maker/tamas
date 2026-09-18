package red.line.callino.ui.themeengine

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import red.line.callino.data.CallTheme

/**
 * VideoThemeRenderer - Renders looping background videos using AndroidX Media3 ExoPlayer.
 * Features:
 * - Infinite Loop
 * - Autoplay
 * - Muted Audio
 * - Fullscreen Zoom / Aspect Fill
 * - Proper Lifecycle & Resource Release
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoThemeRenderer(
    theme: CallTheme,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    if (!theme.mediaUri.isNullOrBlank()) {
        val exoPlayer = remember(theme.mediaUri) {
            ExoPlayer.Builder(context).build().apply {
                val rawUri = theme.mediaUri ?: ""
                val normalizedUri = if (rawUri.startsWith("file:///android_asset/")) {
                    "asset:///" + rawUri.removePrefix("file:///android_asset/")
                } else if (rawUri.startsWith("assets/")) {
                    "asset:///" + rawUri.removePrefix("assets/")
                } else {
                    rawUri
                }
                val uri = Uri.parse(normalizedUri)
                val mediaItem = MediaItem.fromUri(uri)
                setMediaItem(mediaItem)
                repeatMode = Player.REPEAT_MODE_ALL
                volume = 0f // Muted background video
                playWhenReady = true
                prepare()
            }
        }

        // Manage lifecycle events for the ExoPlayer instance
        DisposableEffect(lifecycleOwner, exoPlayer) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        exoPlayer.play()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        exoPlayer.pause()
                    }
                    else -> Unit
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                exoPlayer.release()
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        // Fallback to animated particle / video simulation if no local file URI is present
        GradientThemeRenderer(theme = theme, modifier = modifier)
    }
}
