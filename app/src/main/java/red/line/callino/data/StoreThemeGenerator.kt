package red.line.callino.data

/**
 * تولیدکننده پکیج‌های حرفه‌ای فروشگاه
 * به جای JSON بزرگ، اینجا به صورت کد تولید می‌شن
 */
object StoreThemeGenerator {

    private data class CategoryInfo(
        val nameFa: String,
        val tags: List<String>,
        val colors: List<String>,
        val coverPattern: String,
        val isVip: Boolean
    )

    private val CATEGORIES = listOf(
        CategoryInfo("لوکس 💎", listOf("لوکس", "طلایی", "الماس", "اشرافی"), listOf("#FFD700", "#1E1B4B"), "gold_diamond", true),
        CategoryInfo("طبیعت 🌿", listOf("طبیعت", "جنگل", "دریا", "کوهستان"), listOf("#10B981", "#047857"), "forest", false),
        CategoryInfo("عاشقانه ❤️", listOf("عاشقانه", "قلب", "رز", "رمانتیک"), listOf("#EC4899", "#BE185D"), "heart", true),
        CategoryInfo("فضایی 🚀", listOf("فضا", "کهکشان", "ستاره", "سیاره"), listOf("#312E81", "#6D28D9"), "galaxy", true),
        CategoryInfo("نئون 🌌", listOf("نئون", "سایبرپانک", "الکتریک", "شبانه"), listOf("#06B6D4", "#EC4899"), "neon", true),
        CategoryInfo("انتزاعی 🎨", listOf("انتزاعی", "هنری", "مدرن", "گرادیانت"), listOf("#8B5CF6", "#6366F1"), "abstract", false),
        CategoryInfo("فانتزی 🐉", listOf("فانتزی", "افسانه", "جادو", "دنیای دیگر"), listOf("#7C3AED", "#DB2777"), "fantasy", true),
        CategoryInfo("حیوانات 🐾", listOf("حیوانات", "گربه", "سگ", "پرندگان"), listOf("#F59E0B", "#D97706"), "animals", false),
        CategoryInfo("مینیمال ◾", listOf("مینیمال", "ساده", "تک‌رنگ", "کلاسیک"), listOf("#18181B", "#F4F4F5"), "minimal", false),
        CategoryInfo("کلاسیک 🎩", listOf("کلاسیک", "قدیمی", "سنتی", "وینتج"), listOf("#92400E", "#78350F"), "classic", false),
        CategoryInfo("ایرانی 🏛", listOf("ایرانی", "پارسی", "سنتی", "خطاطی"), listOf("#065F46", "#F59E0B"), "persian", true),
        CategoryInfo("مدرن 🎯", listOf("مدرن", "شیک", "حرفه‌ای", "تجاری"), listOf("#0EA5E9", "#6366F1"), "modern", false)
    )

    private val PACK_NAME_TEMPLATES = listOf(
        "پک %s حرفه‌ای",
        "مجموعه %s لوکس",
        "کالکشن %s ویژه",
        "%s پرمیوم",
        "پکیج %s طلایی",
        "%s اختصاصی",
        "کلکسیون %s نفیس",
        "%s مدرن",
        "گالری %s هنری",
        "%s کامل"
    )

    fun generate(): List<ThemePackage> {
        val result = mutableListOf<ThemePackage>()
        var counter = 1

        CATEGORIES.forEach { cat ->
            // هر دسته ۸ تا ۱۲ پکیج داره
            val countInCategory = 8 + (cat.nameFa.hashCode().absoluteValue % 5)

            repeat(countInCategory) { i ->
                val id = "pkg_${cat.coverPattern}_${counter}"
                val title = String.format(
                    PACK_NAME_TEMPLATES[i % PACK_NAME_TEMPLATES.size],
                    cat.nameFa.substringBefore(" ")
                ) + " ${i + 1}"

                val themesCount = 3 + (i % 6)
                val isVip = cat.isVip || (i % 5 == 0)

                val themeIds = (0 until themesCount).map { idx ->
                    "theme_${cat.coverPattern}_${counter}_$idx"
                }

                val pkg = ThemePackage(
                    id = id,
                    titleFa = title,
                    description = buildDescription(cat.nameFa, themesCount),
                    coverImage = generateCoverPath(cat.coverPattern, i),
                    themesCount = themesCount,
                    isVip = isVip,
                    price = if (isVip) "ویژه اشتراک VIP" else "رایگان",
                    category = cat.nameFa,
                    themeIds = themeIds,
                    tags = cat.tags,
                    downloadStatus = "AVAILABLE"
                )

                result.add(pkg)
                counter++
            }
        }

        return result
    }

    private fun buildDescription(categoryFa: String, count: Int): String {
        return "مجموعه‌ای بی‌نظیر از $count پوسته $categoryFa با کیفیت 4K و طراحی حرفه‌ای، " +
                "مناسب برای شخصی‌سازی کامل صفحه تماس ورودی شما."
    }

    private fun generateCoverPath(pattern: String, index: Int): String {
        // فایل‌های cover توی assets/images هستند
        val images = listOf(
            "807682629_18124159321871593_522428119509321694_n.jpg",
            "808051347_18124159288871593_3749388919559319556_n.jpg",
            "Dreams Above The Clouds.jpg",
            "Nature has a way of making the noise in our minds feel smaller 💚✨...Created using @imagineartof.jpg"
        )
        return "images/${images[index % images.size]}"
    }

    private val Int.absoluteValue: Int get() = if (this < 0) -this else this
}