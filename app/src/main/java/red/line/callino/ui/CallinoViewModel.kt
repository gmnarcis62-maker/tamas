package red.line.callino.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import red.line.callino.billing.LocalVipManager
import red.line.callino.billing.VipTier
import red.line.callino.data.AppSettings
import red.line.callino.data.AssetTheme
import red.line.callino.data.CallTheme
import red.line.callino.data.CallinoRepository
import red.line.callino.data.ContactTheme
import red.line.callino.data.ThemePackage
import red.line.callino.data.ThemeStoreRepository
import red.line.callino.data.ThemeType
import red.line.callino.data.UserMedia
import red.line.callino.data.VipStatus

data class CallinoUiState(
    val themes: List<CallTheme> = emptyList(),
    val assetThemes: List<AssetTheme> = emptyList(),
    val contactThemes: List<ContactTheme> = emptyList(),
    val userMediaList: List<UserMedia> = emptyList(),
    val themePackages: List<ThemePackage> = emptyList(),
    val downloadProgressMap: Map<String, Float> = emptyMap(),
    val settings: AppSettings = AppSettings(),
    val vipStatus: VipStatus = VipStatus(),
    val activeGlobalTheme: CallTheme? = null,
    val previewingTheme: CallTheme? = null,
    val isSimulatingCall: Boolean = false,
    val isPreviewMode: Boolean = false,
    val simulatorCallerName: String = "",
    val simulatorCallerNumber: String = "",
    val simulatorRelationship: String? = "پیش‌نمایش تم"
)

class CallinoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CallinoRepository.getInstance(application)
    private val vipManager = LocalVipManager.getInstance(application)
    private val themeStoreRepo = ThemeStoreRepository.getInstance(application)

    private val servicePrefs = application.getSharedPreferences(
        "callino_prefs", Context.MODE_PRIVATE
    )

    private val _simulatingState = MutableStateFlow(
        SimulatingState(isSimulating = false)
    )

    data class SimulatingState(
        val isSimulating: Boolean = false,
        val isPreviewMode: Boolean = false,
        val customTheme: CallTheme? = null,
        val callerName: String = "",
        val callerNumber: String = "",
        val relationship: String? = "پیش‌نمایش تم"
    )

    val uiState: StateFlow<CallinoUiState> = combine(
        combine(
            repository.allThemes,
            repository.assetThemes,
            repository.allContactThemes,
            themeStoreRepo.packages,
            themeStoreRepo.downloadProgressMap
        ) { themes: List<CallTheme>, assetThemes: List<AssetTheme>, contacts: List<ContactTheme>, pkgs: List<ThemePackage>, progress: Map<String, Float> ->
            StoreCombinedData(themes, assetThemes, contacts, pkgs, progress)
        },
        repository.allUserMedia,
        repository.appSettings,
        repository.vipStatus,
        _simulatingState
    ) { (themes, assetThemes, contactThemes, pkgs, progress), userMedia, settings, vipStatus, simState ->
        val activeTheme = themes.find { it.id == settings.activeGlobalThemeId }
            ?: themes.firstOrNull()
            ?: red.line.callino.data.DefaultCallTheme

        syncServiceFlagToPrefs(settings.isServiceEnabled)

        CallinoUiState(
            themes = themes,
            assetThemes = assetThemes,
            contactThemes = contactThemes,
            userMediaList = userMedia,
            themePackages = pkgs,
            downloadProgressMap = progress,
            settings = settings,
            vipStatus = vipStatus,
            activeGlobalTheme = activeTheme,
            previewingTheme = simState.customTheme ?: activeTheme,
            isSimulatingCall = simState.isSimulating,
            isPreviewMode = simState.isPreviewMode,
            simulatorCallerName = simState.callerName,
            simulatorCallerNumber = simState.callerNumber,
            simulatorRelationship = simState.relationship
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CallinoUiState()
    )

    private fun syncServiceFlagToPrefs(enabled: Boolean) {
        val currentPrefsValue = servicePrefs.getBoolean("isServiceEnabled", false)
        if (currentPrefsValue != enabled) {
            servicePrefs.edit()
                .putBoolean("isServiceEnabled", enabled)
                .apply()
            android.util.Log.d("CallinoViewModel", "Synced service flag to prefs: $enabled")
        }
    }

    private data class StoreCombinedData(
        val themes: List<CallTheme>,
        val assetThemes: List<AssetTheme>,
        val contactThemes: List<ContactTheme>,
        val packages: List<ThemePackage>,
        val downloadProgress: Map<String, Float>
    )

    fun purchaseVipTier(tier: VipTier, onComplete: ((Boolean, String) -> Unit)? = null) {
        viewModelScope.launch {
            val result = vipManager.purchaseVip(tier)
            if (result.isSuccess) {
                onComplete?.invoke(true, "اشتراک ${tier.titleFa} با موفقیت فعال گردید.")
            } else {
                onComplete?.invoke(false, result.exceptionOrNull()?.message ?: "خطا در فرآیند فعال‌سازی")
            }
        }
    }

    fun applyPromoCode(code: String, onComplete: ((Boolean, String) -> Unit)? = null) {
        viewModelScope.launch {
            val result = vipManager.activatePromoCode(code)
            if (result.isSuccess) {
                onComplete?.invoke(true, "کد هدیه با موفقیت تایید و اشتراک ویژه فعال گردید.")
            } else {
                onComplete?.invoke(false, result.exceptionOrNull()?.message ?: "کد وارد شده معتبر نیست.")
            }
        }
    }

    fun restorePurchases(onComplete: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val result = vipManager.restorePurchases()
            onComplete?.invoke(result.getOrDefault(false))
        }
    }

    fun setActiveTheme(themeId: String) {
        viewModelScope.launch {
            repository.setActiveTheme(themeId)
        }
    }

    fun updateSettings(settings: AppSettings) {
        viewModelScope.launch {
            repository.updateSettings(settings)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            repository.resetSettings()
        }
    }

    fun addContactTheme(
        contactId: String,
        name: String,
        number: String,
        themeId: String,
        label: String?
    ) {
        viewModelScope.launch {
            repository.addContactTheme(
                ContactTheme(
                    contactId = contactId,
                    contactName = name,
                    contactNumber = number,
                    themeId = themeId,
                    relationshipLabel = label
                )
            )
        }
    }

    fun removeContactTheme(contactId: String) {
        viewModelScope.launch {
            repository.deleteContactTheme(contactId)
        }
    }

    fun addUserMedia(title: String, uri: String, type: ThemeType, onResult: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val persistentUri = repository.importMediaFile(uri, type)
            if (persistentUri != null) {
                val mediaId = "user_media_${System.currentTimeMillis()}"
                val media = UserMedia(
                    id = mediaId,
                    title = title,
                    uri = persistentUri,
                    mediaType = type
                )
                repository.addUserMedia(media)

                // ذخیره CallTheme متناظر با تنظیمات شخصی‌سازی
                repository.addCustomTheme(media.toCallTheme())

                onResult?.invoke(true)
            } else {
                onResult?.invoke(false)
            }
        }
    }

    fun deleteUserMedia(id: String) {
        viewModelScope.launch {
            repository.deleteUserMedia(id)
        }
    }

    /**
     * بروزرسانی محتوای کاربر (افکت، شدت، تنظیمات ظاهری).
     * هم UserMedia و هم CallTheme متناظر را بروز می‌کند.
     */
    fun updateUserMedia(media: UserMedia) {
        viewModelScope.launch {
            repository.updateUserMedia(media)
            repository.addCustomTheme(media.toCallTheme())
        }
    }

    fun setMediaAsActiveTheme(media: UserMedia) {
        viewModelScope.launch {
            val theme = media.toCallTheme()
            repository.addCustomTheme(theme)
            repository.setActiveTheme(theme.id)
        }
    }

    /** پیش‌نمایش مستقیم یک محتوای کاربر بدون نیاز به فعال‌سازی */
    fun previewUserMedia(media: UserMedia) {
        startThemePreview(theme = media.toCallTheme())
    }

    fun setServiceEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = uiState.value.settings
            repository.updateSettings(current.copy(isServiceEnabled = enabled))

            servicePrefs.edit()
                .putBoolean("isServiceEnabled", enabled)
                .apply()

            android.util.Log.d("CallinoViewModel", "setServiceEnabled($enabled) - synced to prefs")
        }
    }

    fun startThemePreview(theme: CallTheme) {
        _simulatingState.value = SimulatingState(
            isSimulating = true,
            isPreviewMode = true,
            customTheme = theme,
            callerName = "",
            callerNumber = "",
            relationship = "پیش‌نمایش تم"
        )
    }

    fun startCallSimulation(
        theme: CallTheme? = null,
        callerName: String = "",
        callerNumber: String = "",
        relationship: String? = null,
        isPreviewMode: Boolean = false
    ) {
        _simulatingState.value = SimulatingState(
            isSimulating = true,
            isPreviewMode = isPreviewMode,
            customTheme = theme,
            callerName = callerName,
            callerNumber = callerNumber,
            relationship = relationship
        )
    }

    fun downloadThemePackage(themePackage: ThemePackage, onComplete: ((Boolean) -> Unit)? = null) {
        themeStoreRepo.downloadPackage(
            packageId = themePackage.id,
            scope = viewModelScope,
            onComplete = onComplete
        )
    }

    fun stopCallSimulation() {
        _simulatingState.value = SimulatingState(isSimulating = false)
    }
}