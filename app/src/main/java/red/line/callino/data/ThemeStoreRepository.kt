package red.line.callino.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class ThemeStoreRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("callino_store_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "ThemeStoreRepository"
        private const val JSON_FILE = "theme_packages.json"
        private const val KEY_DOWNLOADED_PACKAGES = "downloaded_package_ids"

        val STORE_CATEGORIES = listOf(
            "همه",
            "لوکس 💎",
            "طبیعت 🌿",
            "عاشقانه ❤️",
            "فضایی 🚀",
            "نئون 🌌",
            "انتزاعی 🎨",
            "فانتزی 🐉",
            "حیوانات 🐾",
            "مینیمال ◾",
            "کلاسیک 🎩",
            "ایرانی 🏛",
            "مدرن 🎯"
        )

        @Volatile
        private var INSTANCE: ThemeStoreRepository? = null

        fun getInstance(context: Context): ThemeStoreRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemeStoreRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    private val _packages = MutableStateFlow<List<ThemePackage>>(emptyList())
    val packages: StateFlow<List<ThemePackage>> = _packages.asStateFlow()

    private val _downloadProgressMap = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgressMap: StateFlow<Map<String, Float>> = _downloadProgressMap.asStateFlow()

    init {
        loadPackages()
    }

    fun loadPackages() {
        try {
            val downloadedIds = getDownloadedPackageIds()
            val list = mutableListOf<ThemePackage>()

            Log.d(TAG, "=== شروع بارگذاری پکیج‌های فروشگاه ===")

            // ۱. بارگذاری از JSON (پکیج‌های دست‌ساز)
            try {
                context.assets.open(JSON_FILE).use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
                    val jsonStr = reader.readText()
                    val jsonArray = JSONArray(jsonStr)

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val pkg = parsePackage(obj, downloadedIds)
                        if (pkg != null) list.add(pkg)
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "خطا در خواندن JSON، فقط از داده‌های تولیدی استفاده می‌شود: ${e.message}")
            }

            // ۲. تولید ۱۲۰+ پکیج حرفه‌ای
            val generated = StoreThemeGenerator.generate()
            generated.forEach { pkg ->
                val isDownloaded = downloadedIds.contains(pkg.id)
                list.add(
                    pkg.copy(
                        downloadStatus = if (isDownloaded) "DOWNLOADED" else "AVAILABLE"
                    )
                )
            }

            Log.d(TAG, "=== مجموع ${list.size} پکیج بارگذاری شد ===")
            _packages.value = list

        } catch (e: Exception) {
            Log.e(TAG, "خطای کلی در بارگذاری: ${e.message}", e)
        }
    }

    private fun parsePackage(
        obj: org.json.JSONObject,
        downloadedIds: Set<String>
    ): ThemePackage? {
        return try {
            val id = obj.getString("id")
            val isVip = obj.optBoolean("isVip", false)
            val isDownloaded = downloadedIds.contains(id)

            val themeIdsList = mutableListOf<String>()
            if (obj.has("themeIds")) {
                val idsArray = obj.getJSONArray("themeIds")
                for (j in 0 until idsArray.length()) {
                    themeIdsList.add(idsArray.getString(j))
                }
            }

            val tagsList = mutableListOf<String>()
            if (obj.has("tags")) {
                val tagsArray = obj.getJSONArray("tags")
                for (j in 0 until tagsArray.length()) {
                    tagsList.add(tagsArray.getString(j))
                }
            }

            ThemePackage(
                id = id,
                titleFa = obj.getString("titleFa"),
                description = obj.optString("description", ""),
                coverImage = obj.optString("coverImage", ""),
                themesCount = obj.optInt("themesCount", themeIdsList.size.coerceAtLeast(1)),
                isVip = isVip,
                price = obj.optString("price", if (isVip) "ویژه اشتراک VIP" else "رایگان"),
                category = obj.optString("category", "عمومی"),
                themeIds = themeIdsList,
                tags = tagsList,
                downloadStatus = if (isDownloaded) "DOWNLOADED" else "AVAILABLE"
            )
        } catch (e: Exception) {
            Log.e(TAG, "خطا در پارس یک پکیج: ${e.message}")
            null
        }
    }

    fun getPackageById(packageId: String): ThemePackage? {
        return _packages.value.find { it.id == packageId }
    }

    fun getDownloadedPackageIds(): Set<String> {
        return prefs.getStringSet(KEY_DOWNLOADED_PACKAGES, emptySet()) ?: emptySet()
    }

    fun downloadPackage(
        packageId: String,
        scope: CoroutineScope,
        onProgress: ((Float) -> Unit)? = null,
        onComplete: ((Boolean) -> Unit)? = null
    ) {
        val currentProgress = _downloadProgressMap.value.toMutableMap()
        currentProgress[packageId] = 0.05f
        _downloadProgressMap.value = currentProgress

        scope.launch(Dispatchers.IO) {
            try {
                for (step in 1..10) {
                    delay(120)
                    val progress = (step * 10) / 100f
                    val updated = _downloadProgressMap.value.toMutableMap()
                    updated[packageId] = progress
                    _downloadProgressMap.value = updated
                    onProgress?.invoke(progress)
                }

                val downloaded = getDownloadedPackageIds().toMutableSet()
                downloaded.add(packageId)
                prefs.edit().putStringSet(KEY_DOWNLOADED_PACKAGES, downloaded).apply()

                val updatedProgress = _downloadProgressMap.value.toMutableMap()
                updatedProgress.remove(packageId)
                _downloadProgressMap.value = updatedProgress

                loadPackages()
                onComplete?.invoke(true)
            } catch (e: Exception) {
                Log.e(TAG, "Download failed for $packageId: ${e.message}", e)
                val updatedProgress = _downloadProgressMap.value.toMutableMap()
                updatedProgress.remove(packageId)
                _downloadProgressMap.value = updatedProgress
                onComplete?.invoke(false)
            }
        }
    }
}