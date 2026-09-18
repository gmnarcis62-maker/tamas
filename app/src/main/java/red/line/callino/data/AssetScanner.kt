package red.line.callino.data

import android.content.Context
import android.util.Log

object AssetScanner {

    private const val TAG = "AssetScanner"

    private val IMAGE_EXTENSIONS = setOf(
        "jpg", "jpeg", "png", "webp", "bmp", "gif", "heic", "heif", "avif"
    )

    private val VIDEO_EXTENSIONS = setOf(
        "mp4", "mkv", "webm", "avi", "mov", "3gp", "m4v", "flv", "wmv"
    )

    private val SCAN_FOLDERS = listOf("images", "videos", "animations")

    fun scanAll(context: Context): List<AssetTheme> {
        val result = mutableListOf<AssetTheme>()

        SCAN_FOLDERS.forEach { folder ->
            try {
                val files = context.assets.list(folder) ?: emptyArray()
                files.forEach { fileName ->
                    if (fileName.startsWith(".")) return@forEach

                    val ext = fileName.substringAfterLast('.', "").lowercase()
                    val type = when (ext) {
                        in IMAGE_EXTENSIONS -> ThemeType.IMAGE
                        in VIDEO_EXTENSIONS -> ThemeType.VIDEO
                        else -> return@forEach
                    }

                    val assetPath = "$folder/$fileName"
                    val id = "asset_${folder}_${stableId(fileName)}"

                    val effect = EffectRandomizer.forFileName(fileName)
                    val category = detectCategory(folder, fileName)

                    result.add(
                        AssetTheme(
                            id = id,
                            title = "",
                            type = type,
                            assetPath = assetPath,
                            previewPath = null,
                            category = category,
                            isVip = false,
                            descriptionFa = "",
                            tags = emptyList(),
                            effect = effect
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error scanning assets/$folder: ${e.message}", e)
            }
        }

        Log.d(TAG, "Found ${result.size} asset themes total")
        return result
    }

    private fun stableId(fileName: String): String {
        return abs(fileName.hashCode()).toString(36)
    }

    private fun abs(x: Int): Int = if (x < 0) -x else x

    private fun detectCategory(folder: String, fileName: String): String {
        val lower = fileName.lowercase()
        return when {
            folder == "videos" -> "ویدیویی 🎬"
            folder == "animations" -> "متحرک ✨"
            folder == "images" -> when {
                lower.contains("nature") || lower.contains("forest") || lower.contains("flower") -> "طبیعت 🌿"
                lower.contains("moon") || lower.contains("space") || lower.contains("star") -> "فضایی 🌌"
                lower.contains("cat") || lower.contains("fox") || lower.contains("animal") -> "حیوانات 🐾"
                lower.contains("love") || lower.contains("heart") -> "عاشقانه ❤️"
                else -> "تصویری 🖼"
            }
            else -> "سایر"
        }
    }
}