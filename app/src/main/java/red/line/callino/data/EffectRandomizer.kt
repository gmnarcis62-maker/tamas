package red.line.callino.data

/**
 * انتخاب افکت به‌صورت تصادفی اما «پایدار» برای هر تم.
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

    fun resolve(
        themeId: String,
        defaultEffect: EffectType,
        settings: AppSettings
    ): EffectType {
        return when (settings.effectSelectionMode) {
            EffectSelectionMode.OFF -> EffectType.NONE

            EffectSelectionMode.RANDOM -> forTheme(themeId)

            EffectSelectionMode.MANUAL -> {
                val manual = settings.manualEffectMap[themeId]
                if (manual != null) EffectType.fromName(manual)
                else if (defaultEffect != EffectType.NONE) defaultEffect
                else forTheme(themeId)
            }

            EffectSelectionMode.GLOBAL -> settings.globalEffect
        }
    }

    private fun stableHash(input: String): Long {
        var h = 1125899906842597L
        for (c in input) {
            h = 31L * h + c.code
        }
        return if (h < 0) -h else h
    }
}