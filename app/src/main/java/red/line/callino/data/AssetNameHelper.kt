package red.line.callino.data

/**
 * ساخت اسم زیبا برای تم‌های assets
 * اسم فایل خام هرگز نمایش داده نمی‌شود
 */
object AssetNameHelper {

    /**
     * اگر title خالی یا حاوی نام فایل باشد، یک اسم زیبا برمی‌گرداند
     */
    fun beautifulTitle(theme: CallTheme, index: Int): String {
        // اگر عنوان خالی نیست و شبیه نام فایل نیست، همان را برگردان
        val existing = theme.titleFa
        if (existing.isNotBlank() && !looksLikeFileName(existing)) {
            return existing
        }

        return buildNameFromCategory(theme.category, index, theme.type)
    }

    private fun looksLikeFileName(title: String): Boolean {
        // اگر عنوان شامل اعداد طولانی یا ایموجی ناخواسته است
        val digitsOnly = title.filter { it.isDigit() }
        if (digitsOnly.length >= 6) return true
        if (title.contains("تصویر آماده")) return true
        if (title.contains("ویدیو آماده")) return true
        if (title.contains("انیمیشن آماده")) return true
        if (title.contains("_") && title.contains(".jpg")) return true
        return false
    }

    private fun buildNameFromCategory(category: String, index: Int, type: ThemeType): String {
        val prefix = when {
            category.startsWith("طبیعت") -> "طبیعت"
            category.startsWith("فضایی") -> "کهکشان"
            category.startsWith("حیوانات") -> "حیوان بامزه"
            category.startsWith("عاشقانه") -> "رمانتیک"
            category.startsWith("ویدیویی") -> "ویدیوی سینمایی"
            category.startsWith("متحرک") -> "انیمیشن زنده"
            category.startsWith("تصویری") -> "تصویر هنری"
            category.startsWith("نئون") -> "نئون شبانه"
            category.startsWith("فانتزی") -> "فانتزی جادویی"
            else -> when (type) {
                ThemeType.VIDEO -> "ویدیو"
                ThemeType.ANIMATION -> "انیمیشن"
                ThemeType.IMAGE -> "تصویر"
                ThemeType.CUSTOM -> "پوست"
            }
        }
        val indexFa = toPersianDigits(index)
        return "$prefix $indexFa"
    }

    private fun toPersianDigits(number: Int): String {
        val persianDigits = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        return number.toString().map { char ->
            if (char.isDigit()) persianDigits[char.digitToInt()] else char
        }.joinToString("")
    }
}