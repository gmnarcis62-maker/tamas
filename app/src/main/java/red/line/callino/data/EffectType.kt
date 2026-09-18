package red.line.callino.data

/**
 * تمام افکت‌های پریمیوم تماسینو.
 * افکت کهکشان و افکت‌های قدیمی حذف شده‌اند.
 */
enum class EffectType(val label: String) {
    NONE("بدون افکت"),

    MESH_GRADIENT("مِش گرادیانت"),
    INK_FLOW("جوهر روان"),
    NEBULA("سحابی"),
    PREMIUM_AURORA("شفق پریمیوم"),

    LIQUID_GLASS("شیشه مایع"),
    GLASS_REFRACTION("شکست نور"),
    GLASS_MORPH("شیشه مرف"),

    VOLUMETRIC_BEAMS("نور حجمی"),
    BLOOM("شکوفایی نور"),
    PHOTON_BEAMS("شعاع‌های نوری"),
    PRISM_LIGHT("منشور نور"),
    NEON_PULSE("نبض نئون"),
    ENERGY_AURA("هاله انرژی"),

    HOLOGRAPHIC_FOIL("هولوگرافیک"),
    CHROME_METAL("کروم فلزی"),
    LIQUID_METAL("فلز مایع"),
    CRYSTAL_PRISM("کریستال"),

    CONSTELLATION("صورت فلکی"),
    COSMIC_DUST("غبار کیهانی"),
    CAUSTICS("نور زیر آب"),
    CHROMATIC_ABERRATION("خطای رنگی"),

    GLITCH_NEON("گلیچ نئون"),
    SONIC_WAVE("موج صوتی"),
    DEPTH_PARALLAX("عمق پارالاکس");

    companion object {
        val DEFAULT = MESH_GRADIENT

        fun fromName(name: String?): EffectType =
            entries.firstOrNull { it.name == name } ?: NONE
    }
}