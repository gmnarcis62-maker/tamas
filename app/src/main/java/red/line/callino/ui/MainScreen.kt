package red.line.callino.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import red.line.callino.data.CallTheme
import red.line.callino.ui.screens.AboutScreen
import red.line.callino.ui.screens.CallSimulatorScreen
import red.line.callino.ui.screens.ContactsScreen
import red.line.callino.ui.screens.HomeScreen
import red.line.callino.ui.screens.MyContentScreen
import red.line.callino.ui.screens.SettingsScreen
import red.line.callino.ui.screens.ThemesScreen
import red.line.callino.ui.screens.VipScreen
import red.line.callino.ui.theme.FrostedBg
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTintDeep

enum class CallinoNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String,
    val showInBottomNav: Boolean = true
) {
    HOME("خانه", Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
    THEMES("ظاهر تماس", Icons.Filled.Palette, Icons.Outlined.Palette, "tab_themes"),
    CONTACTS("مخاطبین", Icons.Filled.Contacts, Icons.Outlined.Contacts, "tab_contacts"),
    SETTINGS("تنظیمات", Icons.Filled.Settings, Icons.Outlined.Settings, "tab_settings"),
    MY_CONTENT("محتوای من", Icons.Filled.Palette, Icons.Outlined.Palette, "tab_my_content", showInBottomNav = false),
    ABOUT("درباره ما", Icons.Filled.Home, Icons.Outlined.Home, "tab_about", showInBottomNav = false)
}

@Composable
fun MainScreen(
    viewModel: CallinoViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(CallinoNavTab.HOME.ordinal) }
    var showVipModal by remember { mutableStateOf(false) }
    var selectedThemeForDetail by remember { mutableStateOf<CallTheme?>(null) }

    BackHandler(enabled = selectedTab != CallinoNavTab.HOME.ordinal) {
        selectedTab = CallinoNavTab.HOME.ordinal
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostedBg)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                FrostedGlassBottomNavBar(
                    selectedTabOrdinal = selectedTab,
                    onSelectTab = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    CallinoNavTab.HOME.ordinal -> {
                        HomeScreen(
                            activeTheme = state.activeGlobalTheme,
                            popularThemes = state.themes,
                            customThemesCount = state.userMediaList.size,
                            contactsCount = state.contactThemes.size,
                            settings = state.settings,
                            onToggleService = { enabled -> viewModel.setServiceEnabled(enabled) },
                            onOpenCallSimulator = { viewModel.startCallSimulation() },
                            onNavigateToThemes = { selectedTab = CallinoNavTab.THEMES.ordinal },
                            onNavigateToMyContent = { selectedTab = CallinoNavTab.MY_CONTENT.ordinal },
                            onNavigateToContacts = { selectedTab = CallinoNavTab.CONTACTS.ordinal },
                            onNavigateToGuide = { /* حذف شده */ },
                            onSelectTheme = { theme -> viewModel.setActiveTheme(theme.id) }
                        )
                    }

                    CallinoNavTab.THEMES.ordinal -> {
                        ThemesScreen(
                            themes = state.themes,
                            assetThemes = state.assetThemes,
                            activeThemeId = state.settings.activeGlobalThemeId,
                            vipStatus = state.vipStatus,
                            onSelectTheme = { theme -> viewModel.setActiveTheme(theme.id) },
                            onPreviewTheme = { theme -> viewModel.startThemePreview(theme = theme) },
                            onOpenDetail = { theme -> selectedThemeForDetail = theme },
                            onNavigateToVip = { showVipModal = true }
                        )
                    }

                    CallinoNavTab.MY_CONTENT.ordinal -> {
                        MyContentScreen(
                            userMediaList = state.userMediaList,
                            vipStatus = state.vipStatus,
                            onAddUserMedia = { title, uri, type ->
                                viewModel.addUserMedia(title, uri, type)
                            },
                            onDeleteUserMedia = { id ->
                                viewModel.deleteUserMedia(id)
                            },
                            onUpdateUserMedia = { media ->
                                viewModel.updateUserMedia(media)
                            },
                            onSetAsActiveCallTheme = { media ->
                                viewModel.setMediaAsActiveTheme(media)
                            },
                            onPreviewUserMedia = { media ->
                                viewModel.previewUserMedia(media)
                            },
                            onNavigateToVip = { showVipModal = true }
                        )
                    }

                    CallinoNavTab.CONTACTS.ordinal -> {
                        ContactsScreen(
                            contactThemes = state.contactThemes,
                            allThemes = state.themes,
                            onAssignContactTheme = { contactId, name, number, themeId, label ->
                                viewModel.addContactTheme(contactId, name, number, themeId, label)
                            },
                            onRemoveContactTheme = { contactId -> viewModel.removeContactTheme(contactId) },
                            onSimulateContactCall = { name, number, theme ->
                                viewModel.startCallSimulation(
                                    theme = theme,
                                    callerName = name,
                                    callerNumber = number,
                                    relationship = "مخاطب اختصاصی"
                                )
                            }
                        )
                    }

                    CallinoNavTab.SETTINGS.ordinal -> {
                        SettingsScreen(
                            settings = state.settings,
                            onUpdateSettings = { newSettings -> viewModel.updateSettings(newSettings) },
                            onResetSettings = { viewModel.resetSettings() },
                            onNavigateToGuide = { /* حذف شده */ },
                            onOpenEffectsGallery = { selectedTab = CallinoNavTab.THEMES.ordinal }
                        )
                    }

                    CallinoNavTab.ABOUT.ordinal -> {
                        AboutScreen(
                            settings = state.settings,
                            onOpenVip = { showVipModal = true }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state.isSimulatingCall,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val themeToSimulate = state.previewingTheme ?: state.activeGlobalTheme ?: red.line.callino.data.DefaultCallTheme
            CallSimulatorScreen(
                theme = themeToSimulate,
                settings = state.settings,
                callerName = state.simulatorCallerName,
                callerNumber = state.simulatorCallerNumber,
                relationshipLabel = state.simulatorRelationship,
                isPreviewMode = state.isPreviewMode,
                onClose = { viewModel.stopCallSimulation() }
            )
        }

        AnimatedVisibility(visible = showVipModal, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize().background(FrostedBg)) {
                VipScreen(
                    vipStatus = state.vipStatus,
                    onPurchaseTier = { tier -> viewModel.purchaseVipTier(tier) },
                    onApplyPromoCode = { code -> viewModel.applyPromoCode(code) },
                    onRestorePurchases = { viewModel.restorePurchases() },
                    onClose = { showVipModal = false }
                )
            }
        }
    }
}

@Composable
fun FrostedGlassBottomNavBar(
    selectedTabOrdinal: Int,
    onSelectTab: (Int) -> Unit
) {
    val visibleTabs = CallinoNavTab.values().filter { it.showInBottomNav }

    val effectiveSelected = if (visibleTabs.any { it.ordinal == selectedTabOrdinal }) {
        selectedTabOrdinal
    } else {
        CallinoNavTab.HOME.ordinal
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = Color(0x226750A4)
            )
            .testTag("frosted_bottom_nav"),
        shape = RoundedCornerShape(26.dp),
        color = FrostedGlassSolid,
        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            visibleTabs.forEach { tab ->
                val isSelected = effectiveSelected == tab.ordinal

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectTab(tab.ordinal) }
                        .padding(vertical = 4.dp)
                        .testTag(tab.tag)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) FrostedTintDeep else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.title,
                            tint = if (isSelected) FrostedPrimary else FrostedTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.title,
                        color = if (isSelected) FrostedPrimary else FrostedTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}