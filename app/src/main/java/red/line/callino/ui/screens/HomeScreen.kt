package red.line.callino.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.ui.components.CallThemeBackground
import red.line.callino.ui.components.CallinoCard
import red.line.callino.ui.components.CallinoChip
import red.line.callino.ui.components.CallinoEmptyState
import red.line.callino.ui.components.CallinoGlassCard
import red.line.callino.ui.components.CallinoIconBadge
import red.line.callino.ui.components.CallinoPrimaryButton
import red.line.callino.ui.components.CallinoSectionTitle
import red.line.callino.ui.components.VSpacer
import red.line.callino.ui.theme.Radius
import red.line.callino.ui.theme.Spacing

@Composable
fun HomeScreen(
    activeTheme: CallTheme?,
    popularThemes: List<CallTheme>,
    customThemesCount: Int,
    contactsCount: Int,
    settings: AppSettings,
    onToggleService: (Boolean) -> Unit,
    onOpenCallSimulator: () -> Unit,
    onNavigateToThemes: () -> Unit,
    onNavigateToMyContent: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToGuide: () -> Unit,
    onSelectTheme: (CallTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showHealthDialog by remember { mutableStateOf(false) }

    // --- بررسی مجوزها ---
    val hasPhoneState = androidx.core.content.ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.READ_PHONE_STATE
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    val hasContacts = androidx.core.content.ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.READ_CONTACTS
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    val hasAnswerCalls = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        androidx.core.content.ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ANSWER_PHONE_CALLS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    } else true

    val hasNotification = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        androidx.core.content.ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    } else true

    val hasOverlay = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        android.provider.Settings.canDrawOverlays(context)
    } else true

    val allPermissionsGranted = hasPhoneState && hasContacts && hasAnswerCalls && hasNotification && hasOverlay

    // --- Launcher مجوزها ---
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        onToggleService(true)
        val phoneStateGranted = result[android.Manifest.permission.READ_PHONE_STATE] ?: hasPhoneState
        if (phoneStateGranted) {
            android.widget.Toast.makeText(context, "✅ سرویس تماسینو فعال شد", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            android.widget.Toast.makeText(context, "⚠️ دسترسی تماس را تأیید کنید", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    val isEnabled = settings.isServiceEnabled

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            top = Spacing.lg,
            bottom = Spacing.xl
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.xl)
    ) {
        // ===================================================
        // 1. Service Status Card
        // ===================================================
        item {
            ServiceStatusCard(
                isEnabled = isEnabled,
                onEnable = {
                    onToggleService(true)
                    val neededPerms = mutableListOf<String>()
                    if (!hasPhoneState) neededPerms.add(android.Manifest.permission.READ_PHONE_STATE)
                    if (!hasContacts) neededPerms.add(android.Manifest.permission.READ_CONTACTS)
                    if (!hasAnswerCalls && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        neededPerms.add(android.Manifest.permission.ANSWER_PHONE_CALLS)
                    }
                    if (!hasNotification && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        neededPerms.add(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                    if (neededPerms.isNotEmpty()) {
                        permissionLauncher.launch(neededPerms.toTypedArray())
                    }
                    // درخواست Overlay جداگانه
                    if (!hasOverlay && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        try {
                            val intent = android.content.Intent(
                                android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                android.net.Uri.parse("package:${context.packageName}")
                            )
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                },
                onDisable = {
                    onToggleService(false)
                    android.widget.Toast.makeText(context, "⛔ سرویس غیرفعال شد", android.widget.Toast.LENGTH_SHORT).show()
                },
                onShowHealth = { showHealthDialog = true },
                modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)
            )
        }

        // ===================================================
        // 2. Hero Card (Branding + Preview Button)
        // ===================================================
        item {
            HeroBrandCard(
                onOpenCallSimulator = onOpenCallSimulator,
                modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)
            )
        }

        // ===================================================
        // 3. Active Theme Preview
        // ===================================================
        item {
            Column(modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)) {
                CallinoSectionTitle(
                    title = "تم فعال پیش‌فرض",
                    actionText = "تغییر تم ←",
                    onActionClick = onNavigateToThemes
                )
                VSpacer(10)
                ActiveThemeCard(
                    theme = activeTheme,
                    onClick = onOpenCallSimulator
                )
            }
        }

        // ===================================================
        // 4. Quick Access Grid
        // ===================================================
        item {
            Column(modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)) {
                CallinoSectionTitle(title = "دسترسی سریع")
                VSpacer(10)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    QuickAccessCard(
                        title = "گالری تم",
                        subtitle = "تصویر، ویدیو، انیمیشن",
                        icon = Icons.Default.Palette,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToThemes
                    )
                    QuickAccessCard(
                        title = "تم مخاطبین",
                        subtitle = "$contactsCount مخاطب اختصاصی",
                        icon = Icons.Default.Contacts,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToContacts
                    )
                }
                VSpacer(12)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    QuickAccessCard(
                        title = "محتوای من",
                        subtitle = "$customThemesCount فایل شخصی",
                        icon = Icons.Default.VideoLibrary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToMyContent
                    )
                    QuickAccessCard(
                        title = "راهنما",
                        subtitle = "آموزش و دسترسی‌ها",
                        icon = Icons.Default.Security,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToGuide
                    )
                }
            }
        }

        // ===================================================
        // 5. Popular Themes Carousel
        // ===================================================
        if (popularThemes.isNotEmpty()) {
            item {
                Column {
                    CallinoSectionTitle(
                        title = "محبوب‌ترین پوست‌ها",
                        actionText = "مشاهده همه ←",
                        onActionClick = onNavigateToThemes,
                        modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)
                    )
                    VSpacer(10)
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = Spacing.screenHorizontal),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        items(popularThemes) { theme ->
                            PopularThemeCard(
                                theme = theme,
                                isActive = theme.id == settings.activeGlobalThemeId,
                                onClick = { onSelectTheme(theme) }
                            )
                        }
                    }
                }
            }
        }

        // ===================================================
        // 6. Empty state (اگر هیچ تمی نبود)
        // ===================================================
        if (popularThemes.isEmpty()) {
            item {
                CallinoEmptyState(
                    icon = Icons.Default.Palette,
                    title = "هنوز تمی اضافه نشده",
                    description = "از گالری تم، اولین پوست خود را انتخاب کنید",
                    modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)
                )
            }
        }
    }

    // ===================================================
    // Health Dialog
    // ===================================================
    if (showHealthDialog) {
        HealthCheckDialog(
            hasPhoneState = hasPhoneState,
            hasContacts = hasContacts,
            hasAnswerCalls = hasAnswerCalls,
            hasNotification = hasNotification,
            hasOverlay = hasOverlay,
            isServiceEnabled = isEnabled,
            allGranted = allPermissionsGranted,
            onDismiss = { showHealthDialog = false },
            onRequestPermissions = {
                showHealthDialog = false
                val neededPerms = mutableListOf<String>()
                if (!hasPhoneState) neededPerms.add(android.Manifest.permission.READ_PHONE_STATE)
                if (!hasContacts) neededPerms.add(android.Manifest.permission.READ_CONTACTS)
                if (!hasAnswerCalls && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    neededPerms.add(android.Manifest.permission.ANSWER_PHONE_CALLS)
                }
                if (!hasNotification && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    neededPerms.add(android.Manifest.permission.POST_NOTIFICATIONS)
                }
                if (neededPerms.isNotEmpty()) {
                    permissionLauncher.launch(neededPerms.toTypedArray())
                }
                if (!hasOverlay && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    try {
                        val intent = android.content.Intent(
                            android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            android.net.Uri.parse("package:${context.packageName}")
                        )
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                }
            }
        )
    }
}

// ===================================================
// Components
// ===================================================

@Composable
private fun ServiceStatusCard(
    isEnabled: Boolean,
    onEnable: () -> Unit,
    onDisable: () -> Unit,
    onShowHealth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = if (isEnabled) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.error
    val bgColor = if (isEnabled)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    else MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.lg),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(Spacing.lg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CallinoIconBadge(
                        icon = if (isEnabled) Icons.Default.PhoneInTalk else Icons.Default.Call,
                        size = 44,
                        containerColor = accent.copy(alpha = 0.15f),
                        iconColor = accent
                    )
                    Spacer(Modifier.width(Spacing.md))
                    Column {
                        Text(
                            text = "سرویس تماسینو",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = if (isEnabled) "● فعال" else "● غیرفعال",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = accent
                        )
                    }
                }

                // Health button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.xs))
                        .clickable { onShowHealth() },
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(Radius.xs)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "تست",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            VSpacer(12)

            Text(
                text = if (isEnabled)
                    "صفحه اختصاصی تماسینو هنگام تماس‌های ورودی نمایش داده می‌شود."
                else
                    "برای نمایش تم‌ها روی صفحه تماس، سرویس را فعال کنید.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            VSpacer(14)

            if (!isEnabled) {
                CallinoPrimaryButton(
                    text = "فعال‌سازی سرویس",
                    onClick = onEnable,
                    icon = Icons.Default.Check
                )
            } else {
                CallinoOutlineButton(
                    text = "غیرفعال کردن",
                    onClick = onDisable,
                    icon = Icons.Default.Call,
                    borderColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun HeroBrandCard(
    onOpenCallSimulator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    CallinoGlassCard(
        modifier = modifier,
        gradient = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        ),
        contentPadding = PaddingValues(Spacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val logoResId = context.resources.getIdentifier(
                    "img_callino_icon", "drawable", context.packageName
                )
                if (logoResId != 0) {
                    Image(
                        painter = painterResource(id = logoResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(Radius.sm))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(Radius.sm))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(Modifier.width(Spacing.md))
                Column {
                    Text(
                        text = "تماسینو",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Callino · تیم ردلاین",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(Radius.xs)
            ) {
                Text(
                    text = "کافه‌بازار",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        VSpacer(16)

        Text(
            text = "هر تماس، یک تجربه متفاوت.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        VSpacer(6)
        Text(
            text = "شخصی‌سازی هوشمند صفحه تماس با تصاویر، ویدیوهای زنده و تم‌های اختصاصی مخاطبین.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 20.sp
        )

        VSpacer(18)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(Radius.sm))
                .background(Color.White)
                .clickable { onOpenCallSimulator() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(Spacing.sm))
                Text(
                    text = "پیش‌نمایش زنده صفحه تماس",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ActiveThemeCard(
    theme: CallTheme?,
    onClick: () -> Unit
) {
    val effectiveTheme = theme ?: red.line.callino.data.DefaultCallTheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(Radius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CallThemeBackground(theme = effectiveTheme, dimAlpha = 0.45f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(Radius.xs)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "فعال",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Surface(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(Radius.xs)
                    ) {
                        Text(
                            text = effectiveTheme.category,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = effectiveTheme.titleFa,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = effectiveTheme.descriptionFa,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CallinoCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(Spacing.lg)
    ) {
        CallinoIconBadge(
            icon = icon,
            size = 42,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            iconColor = MaterialTheme.colorScheme.primary
        )
        VSpacer(12)
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun PopularThemeCard(
    theme: CallTheme,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(Radius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 6.dp else 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CallThemeBackground(theme = theme, dimAlpha = 0.35f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (isActive) {
                    Surface(
                        shape = RoundedCornerShape(Radius.xs),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "فعال",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Spacer(Modifier.height(1.dp))
                }

                Column {
                    Text(
                        text = theme.titleFa,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = theme.category,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthCheckDialog(
    hasPhoneState: Boolean,
    hasContacts: Boolean,
    hasAnswerCalls: Boolean,
    hasNotification: Boolean,
    hasOverlay: Boolean,
    isServiceEnabled: Boolean,
    allGranted: Boolean,
    onDismiss: () -> Unit,
    onRequestPermissions: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(Spacing.sm))
                Text(
                    text = "تست فعال بودن تماسینو",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Text(
                    text = "بررسی وضعیت مجوزها و آمادگی نمایش تم:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HealthItemRow(title = "نمایش روی برنامه‌های دیگر", isGranted = hasOverlay)
                HealthItemRow(title = "تشخیص تماس ورودی", isGranted = hasPhoneState)
                HealthItemRow(title = "شناسایی مخاطبین", isGranted = hasContacts)
                HealthItemRow(title = "پاسخگویی با دکمه شیشه‌ای", isGranted = hasAnswerCalls)
                HealthItemRow(title = "اعلان‌ها", isGranted = hasNotification)
                HealthItemRow(
                    title = "وضعیت سرویس",
                    isGranted = isServiceEnabled,
                    customSuccess = "فعال",
                    customFail = "غیرفعال"
                )

                if (!allGranted) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "💡 برای کارکرد کامل، همه مجوزهای بالا را تأیید کنید.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 16.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!allGranted) {
                        onRequestPermissions()
                    } else {
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (!allGranted) "اعطای مجوزها" else "تأیید و بستن")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(Radius.lg)
    )
}

@Composable
private fun HealthItemRow(
    title: String,
    isGranted: Boolean,
    customSuccess: String = "فعال / تأیید شده",
    customFail: String = "غیرفعال / نیاز به تأیید"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.xs))
            .background(
                if (isGranted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (isGranted) "✓ $customSuccess" else "✗ $customFail",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isGranted) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }
    }
}