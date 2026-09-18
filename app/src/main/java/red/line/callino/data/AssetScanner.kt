package red.line.callino.data

import android.content.Context
import android.util.Log

/**
 * اسکنر پوشه‌های assets برای پیدا کردن فایل‌های تصویر، ویدیو و انیمیشن
 * هر فایل (با هر پسوند) به عنوان یک تم شناخته می‌شود
 * نام فایل نمایش داده نمی‌شود، بلکه یک نام زیبا بر اساس دسته و اندیس ساخته می‌شود
 */
object AssetScanner {

    private const val TAG = "AssetScanner"

    private val IMAGE_EXTENSIONS = setOf(
        "jpg", "jpeg", "png", "webp", "bmp", "gif", "heic", "heif", "avif"
    )

    private val VIDEO_EXTENSIONS = setOf(
        "mp4", "mkv", "webm", "avi", "mov", "3gp", "m4v", "flv", "wmv"
    )

    // این پوشه‌ها به عنوان منبع تم اسکن می‌شوند
    private val SCAN_FOLDERS = listOf("images", "videos", "animations")

    /**
     * همه فایل‌های قابل استفاده را از assets برمی‌گرداند
     */
    fun scanAll(context: Context): List<AssetTheme> {
        val result = mutableListOf<AssetTheme>()

        SCAN_FOLDERS.forEach { folder ->
            try {
                val files = context.assets.list(folder) ?: emptyArray()
                files.forEach { fileName ->
                    // نادیده گرفتن فایل‌های مخفی و .keep
                    if (fileName.startsWith(".")) return@forEach

                    val ext = fileName.substringAfterLast('.', "").lowercase()
                    val type = when (ext) {
                        in IMAGE_EXTENSIONS -> ThemeType.IMAGE
                        in VIDEO_EXTENSIONS -> ThemeType.VIDEO
                        else -> return@forEach  // پسوند ناشناخته
                    }

                    val assetPath = "$folder/$fileName"
                    val id = "asset_${folder}_${stableId(fileName)}"

                    val effect = EffectType.fromFileName(fileName)
                    val category = detectCategory(folder, fileName)

                    result.add(
                        AssetTheme(
                            id = id,
                            title = "", // خالی می‌مونه، UI نام زیبا می‌سازه
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

    /**
     * یک شناسه پایدار از اسم فایل (برای id یکتا) می‌سازد
     */
    private fun stableId(fileName: String): String {
        return abs(fileName.hashCode()).toString(36)
    }

    private fun abs(x: Int): Int = if (x < 0) -x else x

    /**
     * دسته‌بندی خودکار بر اساس پوشه و نام فایل
     */
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