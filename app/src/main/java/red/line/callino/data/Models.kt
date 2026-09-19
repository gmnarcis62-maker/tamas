package red.line.callino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_themes")
data class CallTheme(
    @PrimaryKey val id: String,
    val titleFa: String,
    val titleEn: String,
    val type: ThemeType,
    val previewResName: String,
    val mediaResName: String? = null,
    val mediaUri: String? = null,
    val isPremium: Boolean = false,
    val isDefault: Boolean = false,
    val category: String = "عمومی",
    val descriptionFa: String = "",
    val effect: EffectType = EffectType.NONE,
    /** اگر true باشد، افکت انتخابی کاربر همیشه اولویت دارد و EffectRandomizer نادیده گرفته می‌شود */
    val effectIsExplicit: Boolean = false,
    val effectIntensity: Float = 1.0f,
    val backgroundDim: Float = 0.3f,
    val enableBlur: Boolean = false,
    val blurAmount: Float = 10f
)

val DefaultCallTheme = CallTheme(
    id = "theme_callino_aurora",
    titleFa = "شفق نئونی تماسینو",
    titleEn = "Callino Aurora",
    type = ThemeType.ANIMATION,
    previewResName = "anim_aurora",
    isPremium = false,
    isDefault = true,
    category = "پیش‌فرض",
    descriptionFa = "پوسته زنده و روان با افکت‌های نوری زیبا",
    effect = EffectType.PREMIUM_AURORA
)

enum class ThemeType {
    IMAGE,
    VIDEO,
    ANIMATION,
    CUSTOM
}

@Entity(tableName = "contact_themes")
data class ContactTheme(
    @PrimaryKey val contactId: String,
    val contactName: String,
    val contactNumber: String,
    val themeId: String,
    val customPhotoUri: String? = null,
    val relationshipLabel: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * محتوای شخصی کاربر (عکس یا ویدیو از گالری).
 * کاربر می‌تواند برای هر کدام، افکت و تنظیمات ظاهری کاملاً اختصاصی تعیین کند.
 */
@Entity(tableName = "user_media")
data class UserMedia(
    @PrimaryKey val id: String,
    val title: String,
    val uri: String,
    val mediaType: ThemeType,
    val durationMs: Long = 0,
    val addedAt: Long = System.currentTimeMillis(),
    val isSelectedForCall: Boolean = false,

    // ---- شخصی‌سازی کامل ----
    val effect: EffectType = EffectType.NONE,
    val effectIntensity: Float = 1.0f,
    val backgroundDim: Float = 0.3f,
    val enableBlur: Boolean = false,
    val blurAmount: Float = 10f,
    val enableParticles: Boolean = true,
    val enableGlow: Boolean = true
) {
    /**
     * تبدیل به CallTheme برای رندر در صفحه تماس.
     * effectIsExplicit = true یعنی کاربر خودش افکت رو انتخاب کرده
     * و نباید حالت تصادفی روی آن اعمال شود.
     */
    fun toCallTheme(): CallTheme = CallTheme(
        id = "user_media_$id",
        titleFa = title,
        titleEn = title,
        type = mediaType,
        previewResName = uri,
        mediaUri = uri,
        isPremium = false,
        isDefault = false,
        category = "محتوای من 📁",
        descriptionFa = "محتوای شخصی شما",
        effect = effect,
        effectIsExplicit = true,
        effectIntensity = effectIntensity,
        backgroundDim = backgroundDim,
        enableBlur = enableBlur,
        blurAmount = blurAmount
    )
}

data class VipStatus(
    val isVip: Boolean = false,
    val purchaseDate: Long = 0L,
    val expiryDate: Long = 0L,
    val vipType: String = "FREE"
)

sealed class DownloadState {
    object Available : DownloadState()
    data class Downloading(val progress: Float) : DownloadState()
    object Downloaded : DownloadState()
    object Locked : DownloadState()
}

data class ThemePackage(
    val id: String,
    val titleFa: String,
    val description: String,
    val coverImage: String = "",
    val themesCount: Int = 1,
    val isVip: Boolean = false,
    val price: String = "رایگان",
    val category: String = "لوکس 💎",
    val themeIds: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val downloadStatus: String = "AVAILABLE"
)

data class AssetTheme(
    val id: String,
    val title: String,
    val type: ThemeType,
    val assetPath: String,
    val previewPath: String? = null,
    val category: String = "طبیعت 🌿",
    val isVip: Boolean = false,
    val descriptionFa: String = "",
    val tags: List<String> = emptyList(),
    val effect: EffectType = EffectType.NONE
) {
    fun toCallTheme(): CallTheme {
        val mediaUri = when (type) {
            ThemeType.IMAGE -> assetPath
            ThemeType.VIDEO -> "asset:///$assetPath"
            ThemeType.ANIMATION -> assetPath
            ThemeType.CUSTOM -> assetPath
        }
        val preview = if (!previewPath.isNullOrBlank()) {
            previewPath
        } else {
            id
        }
        return CallTheme(
            id = id,
            titleFa = title,
            titleEn = id.replace("asset_", "").replace("_", " "),
            type = type,
            previewResName = preview,
            mediaUri = mediaUri,
            isPremium = isVip,
            category = category,
            descriptionFa = if (descriptionFa.isNotBlank()) descriptionFa else "پوسته پیش‌فرض داخلی تماسینو",
            effect = effect,
            effectIsExplicit = false
        )
    }
}

data class AppSettings(
    val isServiceEnabled: Boolean = false,
    val callerNameVisible: Boolean = true,
    val callerNumberVisible: Boolean = true,
    val callerInfoPosition: String = "TOP",
    val fontSizeScale: Float = 1.0f,
    val buttonStyle: String = "GLASS",
    val buttonLayout: String = "BOTTOM_SIDES",
    val buttonSize: String = "MEDIUM",
    val answerText: String = "پاسخ",
    val rejectText: String = "رد تماس",
    val answerEmoji: String = "📞",
    val rejectEmoji: String = "❌",
    val backgroundDim: Float = 0.3f,
    val backgroundBlur: Float = 0.0f,
    val animationsEnabled: Boolean = true,
    val enableParticles: Boolean = true,
    val enableGlow: Boolean = true,
    val enableBlur: Boolean = false,
    val blurAmount: Float = 10f,
    val enableHaptic: Boolean = true,
    val vibrateOnCall: Boolean = true,
    val activeGlobalThemeId: String = "theme_aurora",

    val effectSelectionMode: EffectSelectionMode = EffectSelectionMode.RANDOM,
    val globalEffect: EffectType = EffectType.MESH_GRADIENT,
    val manualEffectMap: Map<String, String> = emptyMap(),

    val supportEmail: String = "gmnarcis@gmail.com",
    val cafeBazaarPackageUrl: String = "bazaar://details?id=red.line.callino",
    val cafeBazaarWebUrl: String = "https://cafebazaar.ir/app/red.line.callino"
)