package red.line.callino.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * مدیریت تم‌های assets - با اسکن خودکار همه فایل‌ها
 * نام زیبا بر اساس دسته و اندیس ساخته می‌شود
 */
object AssetThemeManager {

    private val _assetThemes = MutableStateFlow<List<AssetTheme>>(emptyList())
    val assetThemes: StateFlow<List<AssetTheme>> = _assetThemes.asStateFlow()

    private var initialized = false

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true

        val scanned = AssetScanner.scanAll(context)

        // نام‌گذاری زیبا بر اساس دسته و اندیس
        val counter = mutableMapOf<String, Int>()
        val named = scanned.map { asset ->
            val count = (counter[asset.category] ?: 0) + 1
            counter[asset.category] = count

            val beautifulName = buildBeautifulName(asset.category, count)

            asset.copy(title = beautifulName)
        }

        _assetThemes.value = named
    }

    fun getAll(): List<AssetTheme> = _assetThemes.value

    fun getByCategory(category: String): List<AssetTheme> {
        return _assetThemes.value.filter { it.category == category }
    }

    fun refresh(context: Context) {
        initialized = false
        initialize(context)
    }

    private fun buildBeautifulName(category: String, index: Int): String {
        val prefix = when {
            category.startsWith("طبیعت") -> "طبیعت"
            category.startsWith("فضایی") -> "کهکشان"
            category.startsWith("حیوانات") -> "حیوان"
            category.startsWith("عاشقانه") -> "رمانتیک"
            category.startsWith("ویدیویی") -> "ویدیو"
            category.startsWith("متحرک") -> "انیمیشن"
            category.startsWith("تصویری") -> "تصویر"
            else -> "پوست"
        }
        return "$prefix شماره $index"
    }
}