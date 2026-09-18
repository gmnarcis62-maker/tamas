package red.line.callino.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AppSettings
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.ui.components.CallThemeBackground
import red.line.callino.ui.theme.CallinoPrimary
import red.line.callino.ui.theme.CallinoSuccess
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedContainer
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedGlassWhite
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextMuted
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep
import red.line.callino.ui.theme.FrostedTintLight

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
    var showHealthDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    // Check actual permissions in context
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

    val allPermissionsGranted = hasPhoneState && hasContacts && hasAnswerCalls && hasNotification

    // Multi-permission launcher
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        onToggleService(true)
        val phoneStateGranted = result[android.Manifest.permission.READ_PHONE_STATE] ?: hasPhoneState
        if (phoneStateGranted) {
            android.widget.Toast.makeText(context, "سرویس تماسینو با موفقیت فعال شد ✅", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            android.widget.Toast.makeText(context, "سرویس فعال شد؛ لطفاً دسترسی تماس را در تنظیمات تایید کنید تا تم نمایش داده شود", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Service Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("service_status_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (settings.isServiceEnabled) FrostedTintLight else FrostedGlassSolid
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (settings.isServiceEnabled) FrostedPrimary.copy(alpha = 0.6f) else FrostedBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.isServiceEnabled) FrostedPrimary.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.15f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (settings.isServiceEnabled) Icons.Default.PhoneInTalk else Icons.Default.Call,
                                    contentDescription = null,
                                    tint = if (settings.isServiceEnabled) FrostedPrimary else Color.Red,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "📞 سرویس تماسینو",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FrostedTextPrimary
                                )
                                Text(
                                    text = if (settings.isServiceEnabled) "🟢 سرویس فعال است" else "🔴 سرویس غیرفعال است",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (settings.isServiceEnabled) CallinoSuccess else Color.Red
                                )
                            }
                        }

                        // Test/Health button
                        Surface(
                            modifier = Modifier.clickable { showHealthDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            color = FrostedTint,
                            border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = FrostedPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "تست وضعیت",
                                    color = FrostedPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (settings.isServiceEnabled)
                            "صفحه اختصاصی تماسینو هنگام تماس‌های ورودی نمایش داده می‌شود."
                        else
                            "تماس‌ها به شکل عادی سیستم نمایش داده می‌شوند. برای نمایش تم‌ها، تماسینو را فعال کنید.",
                        style = MaterialTheme.typography.bodySmall,
                        color = FrostedTextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!settings.isServiceEnabled) {
                            Button(
                                onClick = {
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
                                    } else {
                                        android.widget.Toast.makeText(context, "سرویس تماسینو با موفقیت فعال شد ✅", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("enable_service_button"),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = FrostedPrimary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "✅ فعال کردن تماسینو",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    onToggleService(false)
                                    android.widget.Toast.makeText(context, "سرویس تماسینو غیرفعال شد ⛔", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("disable_service_button"),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red.copy(alpha = 0.85f),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "⛔ غیرفعال کردن",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // App Header & Slogan
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val logoResId = context.resources.getIdentifier("img_callino_icon", "drawable", context.packageName)
                            if (logoResId != 0) {
                                Image(
                                    painter = painterResource(id = logoResId),
                                    contentDescription = "آیکون تماسینو",
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, FrostedBorder, RoundedCornerShape(12.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Brush.linearGradient(listOf(FrostedPrimary, FrostedSecondary))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneInTalk,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "تماسینو | Callino",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = FrostedTextPrimary
                                )
                                Text(
                                    text = "تیم نرم‌افزاری ردلاین سافت البرز",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FrostedTextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = FrostedTint,
                            border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                        ) {
                            Text(
                                text = "کافه‌بازار",
                                color = FrostedPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "هر تماس، یک تجربه متفاوت.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FrostedPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "شخصی‌سازی هوشمند صفحه تماس با تصاویر، ویدیوهای زنده و تم‌های اختصاصی مخاطبین.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FrostedTextSecondary,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Big Action: Preview Call Simulator Button (Central feature)
                    Button(
                        onClick = onOpenCallSimulator,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("preview_call_button"),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FrostedPrimary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "👁 پیش‌نمایش زنده صفحه تماس",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Active Theme Preview Card
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تم فعال پیش‌فرض",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FrostedTextPrimary
                    )
                    Surface(
                        color = FrostedTint,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { onNavigateToThemes() }
                    ) {
                        Text(
                            text = "تغییر تم ←",
                            color = FrostedPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onOpenCallSimulator() },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    val effectiveTheme = activeTheme ?: red.line.callino.data.DefaultCallTheme
                    Box(modifier = Modifier.fillMaxSize()) {
                        CallThemeBackground(
                            theme = effectiveTheme,
                            dimAlpha = 0.4f
                        )

                        // Badge & Info overlay
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = FrostedTintDeep.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = FrostedPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "فعال برای تمام تماس‌ها",
                                            color = FrostedPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Surface(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = effectiveTheme.category,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text(
                                        text = effectiveTheme.titleFa,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = effectiveTheme.descriptionFa,
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 12.sp
                                    )
                                }

                                Surface(
                                    color = FrostedPrimary,
                                    shape = CircleShape,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = "مشاهده",
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
        }

        // Quick Grid Dashboard Hub
        item {
            Text(
                text = "دسترسی سریع",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FrostedTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Feature 1: Themes Gallery
                QuickAccessCard(
                    title = "گالری ظاهر تماس",
                    subtitle = "تصاویر، ویدیو و انیمیشن",
                    icon = Icons.Default.Palette,
                    accentColor = FrostedPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToThemes,
                    testTag = "quick_themes_card"
                )

                // Feature 2: Contacts Customizer
                QuickAccessCard(
                    title = "تم مخاطبین",
                    subtitle = "$contactsCount مخاطب اختصاصی",
                    icon = Icons.Default.Contacts,
                    accentColor = FrostedSecondary,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToContacts,
                    testTag = "quick_contacts_card"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Feature 3: My Content
                QuickAccessCard(
                    title = "محتوای من",
                    subtitle = "$customThemesCount فایل شخصی",
                    icon = Icons.Default.VideoLibrary,
                    accentColor = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMyContent,
                    testTag = "quick_my_content_card"
                )

                // Feature 4: Guide & Troubleshooting
                QuickAccessCard(
                    title = "راهنما و دسترسی‌ها",
                    subtitle = "آموزش کامل ۱۲ مرحله",
                    icon = Icons.Default.Security,
                    accentColor = Color(0xFF059669),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToGuide,
                    testTag = "quick_guide_card"
                )
            }
        }

        // Popular Themes Carousel
        if (popularThemes.isNotEmpty()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "محبوب‌ترین پوسته‌ها",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FrostedTextPrimary
                        )
                        Text(
                            text = "مشاهده همه",
                            color = FrostedPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToThemes() }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(popularThemes) { theme ->
                            val isCurrent = theme.id == settings.activeGlobalThemeId
                            Card(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(190.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onSelectTheme(theme) },
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) FrostedPrimary else FrostedBorder
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CallThemeBackground(theme = theme, dimAlpha = 0.35f)

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        if (isCurrent) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = FrostedPrimary
                                            ) {
                                                Text(
                                                    text = "فعال",
                                                    color = Color.White,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        } else {
                                            Spacer(modifier = Modifier.height(1.dp))
                                        }

                                        Column {
                                            Text(
                                                text = theme.titleFa,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = theme.category,
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showHealthDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showHealthDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = FrostedPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تست فعال بودن تماسینو",
                        fontWeight = FontWeight.Bold,
                        color = FrostedTextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "بررسی وضعیت مجوزها و آمادگی نمایش تم تماس:",
                        style = MaterialTheme.typography.bodySmall,
                        color = FrostedTextSecondary
                    )

                    HealthItemRow(
                        title = "تشخیص تماس ورودی (READ_PHONE_STATE)",
                        isGranted = hasPhoneState
                    )
                    HealthItemRow(
                        title = "شناسایی نام و تصویر مخاطب (READ_CONTACTS)",
                        isGranted = hasContacts
                    )
                    HealthItemRow(
                        title = "پاسخگویی با کلیدهای شیشه‌ای (ANSWER_CALLS)",
                        isGranted = hasAnswerCalls
                    )
                    HealthItemRow(
                        title = "ارسال اعلان و تمام‌صفحه (NOTIFICATIONS)",
                        isGranted = hasNotification
                    )
                    HealthItemRow(
                        title = "وضعیت کلید سرویس تماسینو",
                        isGranted = settings.isServiceEnabled,
                        customSuccess = "فعال",
                        customFail = "غیرفعال"
                    )

                    if (!allPermissionsGranted) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 برای کارکرد بدون نقص، تمام مجوزهای بالا را تأیید کنید.",
                            fontSize = 11.sp,
                            color = FrostedPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showHealthDialog = false
                        if (!allPermissionsGranted) {
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
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedPrimary)
                ) {
                    Text(text = if (!allPermissionsGranted) "اعطای مجوزها" else "تأیید و بستن")
                }
            },
            containerColor = FrostedGlassSolid,
            shape = RoundedCornerShape(20.dp)
        )
    }
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
            .clip(RoundedCornerShape(10.dp))
            .background(if (isGranted) FrostedTintLight else Color.Red.copy(alpha = 0.08f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = FrostedTextPrimary
            )
            Text(
                text = if (isGranted) "✅ $customSuccess" else "❌ $customFail",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isGranted) CallinoSuccess else Color.Red
            )
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(FrostedTintLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FrostedTextPrimary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = FrostedTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
