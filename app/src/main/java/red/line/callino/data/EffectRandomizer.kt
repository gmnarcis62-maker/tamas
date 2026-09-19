package red.line.callino.data

/**
 * انتخاب افکت به‌صورت هوشمند:
 * - اگر تم effectIsExplicit داشته باشد (محتوای شخصی کاربر)، همان افکت استفاده می‌شود.
 * - در حالت GLOBAL، افکت سراسری همه‌جا اعمال می‌شود.
 * - در حالت MANUAL، نقشه‌ی دستی اولویت دارد.
 * - در حالت RANDOM، از hash نام تم استفاده می‌شود.
 */
object EffectRandomizer {

    private val pool: List<EffectType> =
        EffectType.entries.filter { it != EffectType.NONE }

    fun forTheme(themeId: String): EffectType {
        if (pool.isEmpty()) return EffectType.NONE
        val seed = stableHash(themeId)
        return pool[(seed % pool.size).toInt()]
    }

    fun forFileName(fileName: String): EffectType = forTheme(fileName)

    /**
     * حل نهایی افکت بر اساس تنظیمات کاربر.
     */
    fun resolve(
        themeId: String,
        defaultEffect: EffectType,
        settings: AppSettings,
        effectIsExplicit: Boolean = false
    ): EffectType {
        // ۱. خاموش → همیشه NONE
        if (settings.effectSelectionMode == EffectSelectionMode.OFF) {
            return EffectType.NONE
        }

        // ۲. اگر کاربر افکت را روی این تم دستی تنظیم کرده → اولویت مطلق
        if (effectIsExplicit && defaultEffect != EffectType.NONE) {
            return defaultEffect
        }

        // ۳. حالت سراسری → افکت انتخاب‌شده کاربر
        if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL) {
            return settings.globalEffect
        }

        // ۴. حالت دستی → نقشه‌ی دستی
        if (settings.effectSelectionMode == EffectSelectionMode.MANUAL) {
            settings.manualEffectMap[themeId]?.let {
                return EffectType.fromName(it)
            }
        }

        // ۵. اگر تم افکت پیش‌فرض دارد → همان
        if (defaultEffect != EffectType.NONE) {
            return defaultEffect
        }

        // ۶. در نهایت → رندوم پایدار
        return forTheme(themeId)
    }

    private fun stableHash(input: String): Long {
        var h = 1125899906842597L
        for (c in input) {
            h = 31L * h + c.code
        }
        return if (h < 0) -h else h
    }
}