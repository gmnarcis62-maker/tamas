package red.line.callino.data

/**
 * حالت انتخاب افکت پریمیوم برای هر تم.
 */
enum class EffectSelectionMode(val label: String, val description: String) {
    RANDOM("تصادفی هوشمند", "به هر تم یک افکت یکتا تعلق می‌گیرد"),
    MANUAL("انتخاب دستی", "برای هر تم خودتان افکت دلخواه را انتخاب کنید"),
    GLOBAL("سراسری", "یک افکت روی همه‌ی تم‌ها اعمال می‌شود"),
    OFF("خاموش", "هیچ افکتی نمایش داده نمی‌شود");

    companion object {
        fun fromName(name: String?): EffectSelectionMode =
            entries.firstOrNull { it.name == name } ?: RANDOM
    }
}