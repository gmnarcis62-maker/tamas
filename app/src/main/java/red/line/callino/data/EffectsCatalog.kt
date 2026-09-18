package red.line.callino.data

data class EffectInfo(
    val type: EffectType,
    val icon: String,
    val shortDesc: String,
    val category: EffectCategory
)

enum class EffectCategory(val label: String, val emoji: String) {
    COLOR("رنگ و گرادیانت", "🎨"),
    GLASS("شیشه و بلور", "💎"),
    LIGHT("نور", "✨"),
    METAL("فلز و هولوگرام", "🪩"),
    PARTICLE("ذره و بافت", "✨"),
    CYBER("سایبری", "📺")
}

object EffectsCatalog {

    val all: List<EffectInfo> = listOf(
        EffectInfo(
            EffectType.MESH_GRADIENT, "🎨",
            "گرادیانت متحرک با چند رنگ نرم که آرام‌آرام جابه‌جا می‌شود",
            EffectCategory.COLOR
        ),
        EffectInfo(
            EffectType.INK_FLOW, "🖋️",
            "حباب‌های رنگی نرم که مثل جوهر در آب پخش می‌شوند",
            EffectCategory.COLOR
        ),
        EffectInfo(
            EffectType.NEBULA, "🌠",
            "سحابی رنگی فضایی با حرکت آرام و عمق زیاد",
            EffectCategory.COLOR
        ),
        EffectInfo(
            EffectType.PREMIUM_AURORA, "🌌",
            "شفق قطبی با لایه‌های رنگی و بافت نویز رویی",
            EffectCategory.COLOR
        ),
        EffectInfo(
            EffectType.LIQUID_GLASS, "💧",
            "پنل شیشه‌ای مایع شبیه iOS با بازتاب نور",
            EffectCategory.GLASS
        ),
        EffectInfo(
            EffectType.GLASS_REFRACTION, "🔮",
            "شکست نور از پشت لایه‌ی شیشه با گرادیانت مخروطی",
            EffectCategory.GLASS
        ),
        EffectInfo(
            EffectType.GLASS_MORPH, "🔷",
            "شیشه‌ای که شکلش مدام تغییر می‌کند",
            EffectCategory.GLASS
        ),
        EffectInfo(
            EffectType.VOLUMETRIC_BEAMS, "✨",
            "شعاع‌های نوری که از بالا به پایین می‌درخشند",
            EffectCategory.LIGHT
        ),
        EffectInfo(
            EffectType.BLOOM, "🌸",
            "شکوفایی نور از مرکز با درخشش نرم",
            EffectCategory.LIGHT
        ),
        EffectInfo(
            EffectType.PHOTON_BEAMS, "⚡",
            "شعاع‌های نوری از پایین که مثل فوتیوس حرکت می‌کنند",
            EffectCategory.LIGHT
        ),
        EffectInfo(
            EffectType.PRISM_LIGHT, "🔺",
            "منشور نور با رنگین‌کمان چرخان",
            EffectCategory.LIGHT
        ),
        EffectInfo(
            EffectType.NEON_PULSE, "💫",
            "حلقه‌های نئون که از مرکز بیرون می‌زنند",
            EffectCategory.LIGHT
        ),
        EffectInfo(
            EffectType.ENERGY_AURA, "🔆",
            "هاله انرژی متحرک دور عکس مخاطب",
            EffectCategory.LIGHT
        ),
        EffectInfo(
            EffectType.HOLOGRAPHIC_FOIL, "💳",
            "افکت هولوگرام مثل کارت بانکی پریمیوم",
            EffectCategory.METAL
        ),
        EffectInfo(
            EffectType.CHROME_METAL, "🪩",
            "فلز کروم با بازتاب نقره‌ای متحرک",
            EffectCategory.METAL
        ),
        EffectInfo(
            EffectType.LIQUID_METAL, "🌊",
            "فلز مایع با بازتاب و تغییر شکل مداوم",
            EffectCategory.METAL
        ),
        EffectInfo(
            EffectType.CRYSTAL_PRISM, "💎",
            "کریستال چرخان با رنگ‌های منشوری",
            EffectCategory.METAL
        ),
        EffectInfo(
            EffectType.CONSTELLATION, "⭐",
            "شبکه‌ی زنده‌ی ستاره‌ها مثل صورت فلکی",
            EffectCategory.PARTICLE
        ),
        EffectInfo(
            EffectType.COSMIC_DUST, "💫",
            "غبار کیهانی که آرام در فضا شنا می‌کند",
            EffectCategory.PARTICLE
        ),
        EffectInfo(
            EffectType.CAUSTICS, "💦",
            "نور زیر آب با بازتاب متحرک",
            EffectCategory.PARTICLE
        ),
        EffectInfo(
            EffectType.CHROMATIC_ABERRATION, "🌈",
            "خطای رنگی سینمایی مثل لنز دوربین",
            EffectCategory.PARTICLE
        ),
        EffectInfo(
            EffectType.GLITCH_NEON, "📺",
            "گلیچ نئون سایبرپانک با خطوط اسکن",
            EffectCategory.CYBER
        ),
        EffectInfo(
            EffectType.SONIC_WAVE, "🔊",
            "موج صوتی که از مرکز منتشر می‌شود",
            EffectCategory.CYBER
        ),
        EffectInfo(
            EffectType.DEPTH_PARALLAX, "🎭",
            "لایه‌های عمق با حرکت پارالاکس",
            EffectCategory.CYBER
        )
    )

    fun byType(type: EffectType): EffectInfo? =
        all.firstOrNull { it.type == type }

    fun byCategory(cat: EffectCategory): List<EffectInfo> =
        all.filter { it.category == cat }
}