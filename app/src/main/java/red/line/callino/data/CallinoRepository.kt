package red.line.callino.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallinoRepository(private val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.callinoDao()
    private val prefs: SharedPreferences = context.getSharedPreferences("callino_prefs", Context.MODE_PRIVATE)

    val allThemes: Flow<List<CallTheme>> = dao.getAllThemesFlow()
    val allContactThemes: Flow<List<ContactTheme>> = dao.getAllContactThemesFlow()
    val allUserMedia: Flow<List<UserMedia>> = dao.getAllUserMediaFlow()

    private val _assetThemes = MutableStateFlow<List<AssetTheme>>(emptyList())
    val assetThemes: StateFlow<List<AssetTheme>> = _assetThemes.asStateFlow()

    private val _vipStatus = MutableStateFlow(loadVipStatus())
    val vipStatus = _vipStatus.asStateFlow()

    private val _appSettings = MutableStateFlow(loadSettings())
    val appSettings = _appSettings.asStateFlow()

    init {
        AssetThemeManager.initialize(context.applicationContext)

        CoroutineScope(Dispatchers.IO).launch {
            scanAndSyncAssets()
        }
    }

    private fun loadVipStatus(): VipStatus {
        val isVip = prefs.getBoolean("isVip", false)
        val purchaseDate = prefs.getLong("vip_purchase_date", 0L)
        val expiryDate = prefs.getLong("vip_expiry_date", 0L)
        val vipType = prefs.getString("vip_type", "FREE") ?: "FREE"

        val active = if (isVip) {
            if (expiryDate <= 0L) true
            else System.currentTimeMillis() < expiryDate
        } else false

        return VipStatus(
            isVip = active,
            purchaseDate = purchaseDate,
            expiryDate = expiryDate,
            vipType = vipType
        )
    }

    fun getVipStatus(): VipStatus = _vipStatus.value

    fun isVip(): Boolean = _vipStatus.value.isVip

    fun updateVipStatus(status: VipStatus) {
        prefs.edit().apply {
            putBoolean("isVip", status.isVip)
            putLong("vip_purchase_date", status.purchaseDate)
            putLong("vip_expiry_date", status.expiryDate)
            putString("vip_type", status.vipType)
            apply()
        }
        _vipStatus.value = status
    }

    suspend fun scanAndSyncAssets() = withContext(Dispatchers.IO) {
        try {
            AssetThemeManager.refresh(context.applicationContext)
            val loadedAssets: List<AssetTheme> = AssetThemeManager.getAll()
            _assetThemes.value = loadedAssets

            val validAssetIds = loadedAssets.map { it.id }.toSet()

            val allDbThemes = dao.getAllThemes()
            for (dbTheme in allDbThemes) {
                if (!dbTheme.id.startsWith("theme_custom_") &&
                    !dbTheme.id.startsWith("user_media_") &&
                    dbTheme.id !in validAssetIds
                ) {
                    dao.deleteTheme(dbTheme.id)
                }
            }

            for (asset in loadedAssets) {
                dao.insertTheme(asset.toCallTheme())
            }
        } catch (e: Exception) {
            android.util.Log.e("CallinoRepository", "Error scanning assets: ${e.message}", e)
        }
    }

    // ==================================================
    // Settings (خواندن / ذخیره با پشتیبانی از افکت‌ها)
    // ==================================================
    private fun loadSettings(): AppSettings {
        val rawButtonStyle = prefs.getString("buttonStyle", "GLASS") ?: "GLASS"
        val normalizedStyle = when (rawButtonStyle) {
            "MODERN", "CLASSIC", "SWIPE" -> "GLASS"
            else -> rawButtonStyle
        }

        return AppSettings(
            isServiceEnabled = prefs.getBoolean("isServiceEnabled", false),
            callerNameVisible = prefs.getBoolean("callerNameVisible", true),
            callerNumberVisible = prefs.getBoolean("callerNumberVisible", true),
            callerInfoPosition = prefs.getString("callerInfoPosition", "TOP") ?: "TOP",
            fontSizeScale = prefs.getFloat("fontSizeScale", 1.0f),
            buttonStyle = normalizedStyle,
            buttonLayout = prefs.getString("buttonLayout", "BOTTOM_SIDES") ?: "BOTTOM_SIDES",
            buttonSize = prefs.getString("buttonSize", "MEDIUM") ?: "MEDIUM",
            answerText = prefs.getString("answerText", "پاسخ") ?: "پاسخ",
            rejectText = prefs.getString("rejectText", "رد تماس") ?: "رد تماس",
            answerEmoji = prefs.getString("answerEmoji", "📞") ?: "📞",
            rejectEmoji = prefs.getString("rejectEmoji", "❌") ?: "❌",
            backgroundDim = prefs.getFloat("backgroundDim", 0.3f),
            backgroundBlur = prefs.getFloat("backgroundBlur", 0.0f),
            animationsEnabled = prefs.getBoolean("animationsEnabled", true),
            enableParticles = prefs.getBoolean("enableParticles", true),
            enableGlow = prefs.getBoolean("enableGlow", true),
            enableBlur = prefs.getBoolean("enableBlur", false),
            blurAmount = prefs.getFloat("blurAmount", 10f),
            enableHaptic = prefs.getBoolean("enableHaptic", true),
            vibrateOnCall = prefs.getBoolean("vibrateOnCall", true),
            activeGlobalThemeId = prefs.getString("activeGlobalThemeId", "theme_aurora") ?: "theme_aurora",

            effectSelectionMode = EffectSelectionMode.fromName(
                prefs.getString("effectSelectionMode", EffectSelectionMode.RANDOM.name)
            ),
            globalEffect = EffectType.fromName(
                prefs.getString("globalEffect", EffectType.MESH_GRADIENT.name)
            ),
            manualEffectMap = decodeEffectMap(prefs.getString("manualEffectMap", "") ?: ""),

            supportEmail = prefs.getString("supportEmail", "gmnarcis@gmail.com") ?: "gmnarcis@gmail.com",
            cafeBazaarPackageUrl = "bazaar://details?id=red.line.callino",
            cafeBazaarWebUrl = "https://cafebazaar.ir/app/red.line.callino"
        )
    }

    fun updateSettings(newSettings: AppSettings) {
        prefs.edit().apply {
            putBoolean("isServiceEnabled", newSettings.isServiceEnabled)
            putBoolean("callerNameVisible", newSettings.callerNameVisible)
            putBoolean("callerNumberVisible", newSettings.callerNumberVisible)
            putString("callerInfoPosition", newSettings.callerInfoPosition)
            putFloat("fontSizeScale", newSettings.fontSizeScale)
            putString("buttonStyle", newSettings.buttonStyle)
            putString("buttonLayout", newSettings.buttonLayout)
            putString("buttonSize", newSettings.buttonSize)
            putString("answerText", newSettings.answerText)
            putString("rejectText", newSettings.rejectText)
            putString("answerEmoji", newSettings.answerEmoji)
            putString("rejectEmoji", newSettings.rejectEmoji)
            putFloat("backgroundDim", newSettings.backgroundDim)
            putFloat("backgroundBlur", newSettings.backgroundBlur)
            putBoolean("animationsEnabled", newSettings.animationsEnabled)
            putBoolean("enableParticles", newSettings.enableParticles)
            putBoolean("enableGlow", newSettings.enableGlow)
            putBoolean("enableBlur", newSettings.enableBlur)
            putFloat("blurAmount", newSettings.blurAmount)
            putBoolean("enableHaptic", newSettings.enableHaptic)
            putBoolean("vibrateOnCall", newSettings.vibrateOnCall)
            putString("activeGlobalThemeId", newSettings.activeGlobalThemeId)

            putString("effectSelectionMode", newSettings.effectSelectionMode.name)
            putString("globalEffect", newSettings.globalEffect.name)
            putString("manualEffectMap", encodeEffectMap(newSettings.manualEffectMap))

            putString("supportEmail", newSettings.supportEmail)
            apply()
        }
        _appSettings.value = newSettings
    }

    /** کدگذاری Map<String, String> به یک رشته‌ی ساده برای ذخیره در SharedPreferences */
    private fun encodeEffectMap(map: Map<String, String>): String {
        if (map.isEmpty()) return ""
        return map.entries.joinToString(";;") { "${it.key}||${it.value}" }
    }

    /** بازگشایی رشته به Map<String, String> */
    private fun decodeEffectMap(raw: String): Map<String, String> {
        if (raw.isBlank()) return emptyMap()
        return raw.split(";;").mapNotNull { entry ->
            val parts = entry.split("||", limit = 2)
            if (parts.size == 2) parts[0] to parts[1] else null
        }.toMap()
    }

    fun getButtonSettings(): AppSettings = _appSettings.value

    fun updateButtonStyle(style: String) {
        val current = _appSettings.value
        updateSettings(current.copy(buttonStyle = style))
    }

    fun updateButtonLayout(layout: String) {
        val current = _appSettings.value
        updateSettings(current.copy(buttonLayout = layout))
    }

    fun updateButtonText(answerText: String, rejectText: String) {
        val current = _appSettings.value
        updateSettings(current.copy(answerText = answerText, rejectText = rejectText))
    }

    fun updateButtonEmoji(answerEmoji: String, rejectEmoji: String) {
        val current = _appSettings.value
        updateSettings(current.copy(answerEmoji = answerEmoji, rejectEmoji = rejectEmoji))
    }

    fun updateButtonSize(size: String) {
        val current = _appSettings.value
        updateSettings(current.copy(buttonSize = size))
    }

    fun resetSettings() {
        prefs.edit().clear().apply()
        _appSettings.value = AppSettings()
    }

    suspend fun getThemeById(themeId: String): CallTheme? = dao.getThemeById(themeId)

    suspend fun getContactThemeForNumber(number: String): ContactTheme? = dao.getContactThemeForNumber(number)

    suspend fun setActiveTheme(themeId: String) {
        val current = _appSettings.value
        updateSettings(current.copy(activeGlobalThemeId = themeId))
    }

    suspend fun addContactTheme(contactTheme: ContactTheme) = dao.insertContactTheme(contactTheme)

    suspend fun deleteContactTheme(contactId: String) = dao.deleteContactTheme(contactId)

    // ==================================================
    // Media import / user media
    // ==================================================
    suspend fun importMediaFile(inputUriString: String, type: ThemeType): String? = withContext(Dispatchers.IO) {
        try {
            val uri = android.net.Uri.parse(inputUriString)
            val mediaDir = java.io.File(context.filesDir, "callino_media").apply {
                if (!exists()) mkdirs()
            }

            val mimeType = try { context.contentResolver.getType(uri) } catch (e: Exception) { null }
            val extension = when {
                mimeType != null && mimeType.contains("png", ignoreCase = true) -> "png"
                mimeType != null && mimeType.contains("webp", ignoreCase = true) -> "webp"
                mimeType != null && mimeType.contains("gif", ignoreCase = true) -> "gif"
                mimeType != null && mimeType.contains("jpeg", ignoreCase = true) -> "jpg"
                mimeType != null && mimeType.contains("jpg", ignoreCase = true) -> "jpg"
                mimeType != null && mimeType.contains("video", ignoreCase = true) -> "mp4"
                type == ThemeType.VIDEO -> "mp4"
                else -> "jpg"
            }

            val uniqueName = "media_${System.currentTimeMillis()}_${java.util.UUID.randomUUID().toString().take(8)}.$extension"
            val destFile = java.io.File(mediaDir, uniqueName)

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                destFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return@withContext null

            if (destFile.exists() && destFile.length() > 0L) {
                android.net.Uri.fromFile(destFile).toString()
            } else {
                if (destFile.exists()) destFile.delete()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveUserMediaFile(inputUriString: String, type: ThemeType): String? =
        importMediaFile(inputUriString, type)

    suspend fun addUserMedia(media: UserMedia) = dao.insertUserMedia(media)

    suspend fun updateUserMedia(media: UserMedia) = dao.updateUserMedia(media)

    suspend fun deleteUserMedia(id: String) = withContext(Dispatchers.IO) {
        try {
            val media = dao.getUserMediaById(id)
            if (media != null) {
                val uri = android.net.Uri.parse(media.uri)
                if (uri.scheme == "file" && uri.path != null) {
                    val file = java.io.File(uri.path!!)
                    if (file.exists()) file.delete()
                } else {
                    val directFile = java.io.File(media.uri)
                    if (directFile.exists()) directFile.delete()
                }
            }
        } catch (e: Exception) {
            // Ignore
        }
        dao.deleteUserMedia(id)
        dao.deleteTheme("theme_custom_$id")
        dao.deleteTheme("user_media_$id")
    }

    suspend fun addCustomTheme(theme: CallTheme) = dao.insertTheme(theme)

    companion object {
        @Volatile
        private var INSTANCE: CallinoRepository? = null

        fun getInstance(context: Context): CallinoRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = CallinoRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}