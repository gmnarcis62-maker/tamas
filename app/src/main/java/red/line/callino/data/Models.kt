package red.line.callino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_themes")
data class CallTheme(
    @PrimaryKey val id: String,
    val titleFa: String,
    val titleEn: String,
    val type: ThemeType, // IMAGE, VIDEO, ANIMATION, CUSTOM
    val previewResName: String,
    val mediaResName: String? = null,
    val mediaUri: String? = null,
    val isPremium: Boolean = false,
    val isDefault: Boolean = false,
    val category: String = "عمومی",
    val descriptionFa: String = ""
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
    descriptionFa = "پوسته زنده و روان با افکت‌های نوری زیبا"
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
    val relationshipLabel: String? = null, // مادر، پدر، همسر، دوست، همکار
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_media")
data class UserMedia(
    @PrimaryKey val id: String,
    val title: String,
    val uri: String,
    val mediaType: ThemeType, // IMAGE or VIDEO
    val durationMs: Long = 0,
    val addedAt: Long = System.currentTimeMillis(),
    val isSelectedForCall: Boolean = false
)

data class VipStatus(
    val isVip: Boolean = false,
    val purchaseDate: Long = 0L,
    val expiryDate: Long = 0L,
    val vipType: String = "FREE" // FREE, MONTHLY, THREE_MONTHS, LIFETIME, PROMO
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
    val downloadStatus: String = "AVAILABLE" // AVAILABLE, DOWNLOADING, DOWNLOADED, LOCKED
)

data class AssetTheme(
    val id: String,
    val title: String,
    val type: ThemeType, // IMAGE, VIDEO, ANIMATION
    val assetPath: String,
    val previewPath: String? = null,
    val category: String = "طبیعت 🌿",
    val isVip: Boolean = false,
    val descriptionFa: String = "",
    val tags: List<String> = emptyList()
) {
    fun toCallTheme(): CallTheme {
        val mediaUri = when (type) {
            ThemeType.IMAGE -> "file:///android_asset/$assetPath"
            ThemeType.VIDEO -> "asset:///$assetPath"
            ThemeType.ANIMATION -> "file:///android_asset/$assetPath"
            ThemeType.CUSTOM -> assetPath
        }
        val preview = if (!previewPath.isNullOrBlank()) {
            "file:///android_asset/$previewPath"
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
            descriptionFa = if (descriptionFa.isNotBlank()) descriptionFa else "پوسته پیش‌فرض داخلی تماسینو"
        )
    }
}

data class AppSettings(
    val isServiceEnabled: Boolean = false,
    val callerNameVisible: Boolean = true,
    val callerNumberVisible: Boolean = true,
    val callerInfoPosition: String = "TOP", // TOP, CENTER, BOTTOM
    val fontSizeScale: Float = 1.0f,
    val buttonStyle: String = "GLASS", // GLASS, CRYSTAL, NEON, ROUNDED, PILL, MINIMAL
    val buttonLayout: String = "BOTTOM_SIDES", // BOTTOM_SIDES, BOTTOM_CENTER, SCREEN_SIDES, VERTICAL
    val buttonSize: String = "MEDIUM", // SMALL, MEDIUM, LARGE
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
    val supportEmail: String = "gmnarcis@gmail.com",
    val cafeBazaarPackageUrl: String = "bazaar://details?id=red.line.callino",
    val cafeBazaarWebUrl: String = "https://cafebazaar.ir/app/red.line.callino"
)
