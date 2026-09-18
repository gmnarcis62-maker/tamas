package red.line.callino.data

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * AssetThemeManager - Handles reading pre-packaged static and dynamic theme assets
 * bundled directly inside the APK.
 *
 * It first reads metadata from `assets/themes.json` (Catalog Metadata).
 * If extra unindexed files exist in `assets/images/`, `assets/videos/`, `assets/animations/`,
 * they are gracefully discovered and added.
 */
class AssetThemeManager(private val context: Context) {

    private val assetManager: AssetManager = context.assets

    companion object {
        private const val TAG = "AssetThemeManager"
        const val JSON_FILE = "themes.json"
        const val DIR_IMAGES = "images"
        const val DIR_VIDEOS = "videos"
        const val DIR_ANIMATIONS = "animations"

        val STORE_CATEGORIES = listOf(
            "همه",
            "طبیعت 🌿",
            "نئون 🌌",
            "عاشقانه ❤️",
            "لوکس 💎",
            "فضایی 🚀",
            "مینیمال",
            "مناسبتی"
        )

        @Volatile
        private var INSTANCE: AssetThemeManager? = null

        fun getInstance(context: Context): AssetThemeManager {
            return INSTANCE ?: synchronized(this) {
                val instance = AssetThemeManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Lists all available asset themes.
     * 1. Loads from themes.json
     * 2. Scans directories for any unlisted files to provide 100% dynamic developer experience.
     */
    fun loadAllAssetThemes(): List<AssetTheme> {
        val themes = mutableListOf<AssetTheme>()
        val loadedIds = mutableSetOf<String>()

        // 1. Read themes.json first
        val fromJson = loadThemesFromJson()
        for (item in fromJson) {
            themes.add(item)
            loadedIds.add(item.id)
            loadedIds.add(item.assetPath)
        }

        // 2. Scan directories for any newly placed assets not yet in JSON
        val scanned = scanAssetDirectories(loadedIds)
        themes.addAll(scanned)

        return themes
    }

    /**
     * Reads and parses `assets/themes.json`
     */
    fun loadThemesFromJson(): List<AssetTheme> {
        val list = mutableListOf<AssetTheme>()
        try {
            val inputStream = assetManager.open(JSON_FILE)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }.trim()
            if (jsonString.isEmpty()) return list

            val jsonArray = if (jsonString.startsWith("{")) {
                val rootObj = JSONObject(jsonString)
                rootObj.optJSONArray("themes") ?: JSONArray()
            } else {
                JSONArray(jsonString)
            }

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", "theme_$i")
                val titleFa = obj.optString("titleFa", "پوسته تماسینو $i")
                val typeStr = obj.optString("type", "IMAGE")
                val path = obj.optString("path", "")
                val category = obj.optString("category", "طبیعت 🌿")
                val isVip = obj.optBoolean("isVip", false)
                val preview = obj.optString("preview", "")
                val descriptionFa = obj.optString("descriptionFa", "")

                val tagsList = mutableListOf<String>()
                val tagsJson = obj.optJSONArray("tags")
                if (tagsJson != null) {
                    for (j in 0 until tagsJson.length()) {
                        tagsList.add(tagsJson.getString(j))
                    }
                }

                val type = when (typeStr.uppercase()) {
                    "VIDEO" -> ThemeType.VIDEO
                    "ANIMATION" -> ThemeType.ANIMATION
                    "CUSTOM" -> ThemeType.CUSTOM
                    else -> ThemeType.IMAGE
                }

                list.add(
                    AssetTheme(
                        id = id,
                        title = titleFa,
                        type = type,
                        assetPath = path,
                        previewPath = if (preview.isNotBlank()) preview else null,
                        category = category,
                        isVip = isVip,
                        descriptionFa = descriptionFa,
                        tags = tagsList
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed reading $JSON_FILE: ${e.message}", e)
        }
        return list
    }

    /**
     * Scans directories for any assets not present in themes.json
     */
    private fun scanAssetDirectories(existingKeys: Set<String>): List<AssetTheme> {
        val list = mutableListOf<AssetTheme>()
        list.addAll(scanDir(DIR_IMAGES, ThemeType.IMAGE, existingKeys))
        list.addAll(scanDir(DIR_VIDEOS, ThemeType.VIDEO, existingKeys))
        list.addAll(scanDir(DIR_ANIMATIONS, ThemeType.ANIMATION, existingKeys))
        return list
    }

    private fun scanDir(dirName: String, type: ThemeType, existingKeys: Set<String>): List<AssetTheme> {
        val list = mutableListOf<AssetTheme>()
        try {
            val files = assetManager.list(dirName) ?: emptyArray()
            for (filename in files) {
                // Ignore .keep, placeholder files, and hidden files
                if (filename.startsWith(".") ||
                    filename.equals(".keep", ignoreCase = true) ||
                    filename.startsWith("placeholder", ignoreCase = true)
                ) {
                    continue
                }

                val fullPath = "$dirName/$filename"
                val fileObj = File(filename)
                val ext = fileObj.extension.lowercase()
                val nameWithoutExt = fileObj.nameWithoutExtension

                // Ensure matching extension per directory type
                val isValid = when (dirName) {
                    DIR_IMAGES -> ext in listOf("webp", "png", "jpg", "jpeg", "gif", "bmp")
                    DIR_VIDEOS -> ext in listOf("mp4", "mkv", "webm", "3gp")
                    DIR_ANIMATIONS -> ext in listOf("json", "lottie")
                    else -> true
                }
                if (!isValid) continue

                val id = "asset_${dirName}_${nameWithoutExt.lowercase().replace(" ", "_")}"

                if (id in existingKeys || fullPath in existingKeys) {
                    continue
                }

                val formattedTitle = formatPersianTitle(nameWithoutExt, type)
                val category = categorizeByKeyword(nameWithoutExt)
                val isVip = type == ThemeType.VIDEO || filename.contains("vip", ignoreCase = true) || filename.contains("premium", ignoreCase = true)

                list.add(
                    AssetTheme(
                        id = id,
                        title = formattedTitle,
                        type = type,
                        assetPath = fullPath,
                        previewPath = if (type == ThemeType.IMAGE) fullPath else null,
                        category = category,
                        isVip = isVip,
                        descriptionFa = "پوسته آماده تماسینو در دسته $category"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scanning asset directory $dirName: ${e.message}", e)
        }
        return list
    }

    private fun categorizeByKeyword(name: String): String {
        val lower = name.lowercase()
        return when {
            lower.contains("neon") || lower.contains("cyber") || lower.contains("glow") -> "نئون 🌌"
            lower.contains("nature") || lower.contains("forest") || lower.contains("tree") || lower.contains("leaf") -> "طبیعت 🌿"
            lower.contains("love") || lower.contains("rose") || lower.contains("heart") -> "عاشقانه ❤️"
            lower.contains("luxury") || lower.contains("gold") || lower.contains("diamond") -> "لوکس 💎"
            lower.contains("space") || lower.contains("galaxy") || lower.contains("star") || lower.contains("cosmos") -> "فضایی 🚀"
            lower.contains("minimal") || lower.contains("dark") || lower.contains("glass") -> "مینیمال"
            lower.contains("festive") || lower.contains("party") || lower.contains("nowruz") || lower.contains("celebration") -> "مناسبتی"
            else -> "طبیعت 🌿"
        }
    }

    private fun formatPersianTitle(nameWithoutExt: String, type: ThemeType): String {
        val clean = nameWithoutExt.replace("_", " ").replace("-", " ")
        return when {
            clean.contains("gold", ignoreCase = true) -> "طلایی درخشان"
            clean.contains("aurora", ignoreCase = true) -> "شفق قطبی"
            clean.contains("cyber", ignoreCase = true) || clean.contains("neon", ignoreCase = true) -> "سایبر نئون"
            clean.contains("cosmos", ignoreCase = true) || clean.contains("space", ignoreCase = true) -> "کیهان بیکران"
            clean.contains("rose", ignoreCase = true) || clean.contains("love", ignoreCase = true) -> "رز رمانتیک"
            clean.contains("particle", ignoreCase = true) -> "ذرات معلق نورانی"
            clean.contains("wave", ignoreCase = true) -> "امواج متحرک"
            clean.contains("minimal", ignoreCase = true) -> "مینیمال شیک"
            else -> when (type) {
                ThemeType.IMAGE -> "تصویر آماده $clean"
                ThemeType.VIDEO -> "ویدیو آماده $clean"
                ThemeType.ANIMATION -> "انیمیشن آماده $clean"
                ThemeType.CUSTOM -> clean
            }
        }
    }
}
