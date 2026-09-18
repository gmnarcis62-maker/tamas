package red.line.callino.data

import kotlin.math.abs

/**
 * انواع افکت‌های نمایش برای تم‌های تصویری/ویدیویی/انیمیشن
 * هر فایل موجود در assets یک افکت رندوم (بر اساس هش نامش) می‌گیرد
 */
enum class EffectType(val id: Int, val titleFa: String) {
    NONE(0, "بدون افکت"),
    FADE_LOOP(1, "محو شدن دوره‌ای"),
    SCALE_BREATHE(2, "نفس کشیدن آرام"),
    PULSE_GLOW(3, "درخشش پالسی"),
    ROTATE_SLOW(4, "چرخش آرام"),
    COLOR_SHIFT(5, "تغییر رنگ"),
    BLUR_PULSE(6, "تاری پالسی"),
    GRADIENT_SHIFT(7, "جابجایی گرادیانت"),
    PARTICLES(8, "ذرات شناور"),
    SHAKE_SOFT(9, "لرزش ملایم"),
    ZOOM_IN_OUT(10, "زوم داخل و خارج"),
    FLIP_HORIZONTAL(11, "چرخش افقی"),
    WAVE(12, "موج"),
    NEON_BORDER(13, "حاشیه نئون"),
    FILM_GRAIN(14, "گرین فیلم");

    companion object {
        /**
         * یک افکت رندوم (اما تکرارپذیر) از روی نام فایل تولید می‌کند
         */
        fun fromFileName(fileName: String): EffectType {
            val hash = abs(fileName.hashCode())
            // NONE رو حذف می‌کنیم تا همیشه یه افکت داشته باشه
            val types = values().filter { it != NONE }
            return types[hash % types.size]
        }
    }
}