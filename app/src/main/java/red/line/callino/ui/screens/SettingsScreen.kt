package red.line.callino.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AppSettings
import red.line.callino.ui.components.CallButtonsContainer
import red.line.callino.ui.theme.CallinoDanger
import red.line.callino.ui.theme.CallinoSuccess
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedContainer
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextMuted
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep
import red.line.callino.ui.theme.FrostedTintLight

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdateSettings: (AppSettings) -> Unit,
    onResetSettings: () -> Unit,
    onNavigateToGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "⚙ شخصی‌سازی و تنظیمات",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "تنظیم جزئیات صفحه تماس، وضعیت سرویس و افکت‌های بصری",
                    style = MaterialTheme.typography.bodySmall,
                    color = FrostedTextSecondary
                )
            }
        }

        // Service Master Switch Group
        item {
            SettingsSectionCard(title = "وضعیت سرویس تماسینو", icon = Icons.Default.PhoneInTalk) {
                SettingSwitchRow(
                    title = "فعال بودن صفحه تماس تماسینو",
                    subtitle = if (settings.isServiceEnabled) "سرویس فعال است و صفحه تم هنگام تماس نمایش داده می‌شود" else "سرویس غیرفعال است (تماس‌ها به شکل عادی سیستم نمایش داده می‌شوند)",
                    checked = settings.isServiceEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(isServiceEnabled = it)) },
                    tag = "toggle_service_master"
                )
            }
        }

        // Display Info Group
        item {
            SettingsSectionCard(title = "اطلاعات تماس‌گیرنده", icon = Icons.Default.Visibility) {
                // Show caller name
                SettingSwitchRow(
                    title = "نمایش نام مخاطب",
                    subtitle = "نمایش نام مخاطب بالای صفحه تماس",
                    checked = settings.callerNameVisible,
                    onCheckedChange = { onUpdateSettings(settings.copy(callerNameVisible = it)) },
                    tag = "toggle_caller_name"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Show caller number
                SettingSwitchRow(
                    title = "نمایش شماره تماس",
                    subtitle = "نمایش شماره یا خط تماس ورودی",
                    checked = settings.callerNumberVisible,
                    onCheckedChange = { onUpdateSettings(settings.copy(callerNumberVisible = it)) },
                    tag = "toggle_caller_number"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Position Selector (Top, Center, Bottom)
                Text(text = "موقعیت اطلاعات تماس:", color = FrostedTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "TOP" to "بالا (استاندارد)",
                        "CENTER" to "مرکز صفحه",
                        "BOTTOM" to "پایین"
                    ).forEach { (posKey, posLabel) ->
                        val isSel = settings.callerInfoPosition == posKey
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(callerInfoPosition = posKey)) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) FrostedPrimary else FrostedTintLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) FrostedPrimary else FrostedBorder)
                        ) {
                            Text(
                                text = posLabel,
                                color = if (isSel) Color.White else FrostedTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Font Size Scale
                Text(
                    text = "اندازه قلم نام و نوشته‌ها: ${(settings.fontSizeScale * 100).toInt()}٪",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp
                )
                Slider(
                    value = settings.fontSizeScale,
                    onValueChange = { onUpdateSettings(settings.copy(fontSizeScale = it)) },
                    valueRange = 0.8f..1.3f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = FrostedPrimary,
                        activeTrackColor = FrostedPrimary,
                        inactiveTrackColor = FrostedBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("font_size_slider")
                )
            }
        }

        // Button Styles & Design Group
        item {
            SettingsSectionCard(title = "🎨 طراحی دکمه‌های تماس", icon = Icons.Default.Palette) {
                // 1. Live Preview Card
                Text(
                    text = "پیش‌نمایش زنده دکمه‌ها:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                            )
                        )
                        .border(1.dp, FrostedBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Render Live Interactive Call Buttons
                    CallButtonsContainer(
                        settings = settings,
                        onAnswerClick = {},
                        onRejectClick = {}
                    )

                    // Badge
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "پیش‌نمایش زنده",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 2. Select Style (6 Styles)
                Text(
                    text = "استایل دکمه‌ها:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val styles = listOf(
                    "GLASS" to "شیشه‌ای",
                    "CRYSTAL" to "کریستال",
                    "NEON" to "نئون",
                    "ROUNDED" to "گرد",
                    "PILL" to "کپسولی",
                    "MINIMAL" to "مینیمال"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        styles.take(3).forEach { (sKey, sLabel) ->
                            val isSel = settings.buttonStyle.equals(sKey, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onUpdateSettings(settings.copy(buttonStyle = sKey)) }
                                    .testTag("style_btn_$sKey"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) FrostedPrimary else FrostedTintLight,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) FrostedPrimary else FrostedBorder
                                )
                            ) {
                                Text(
                                    text = sLabel,
                                    color = if (isSel) Color.White else FrostedTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        styles.drop(3).forEach { (sKey, sLabel) ->
                            val isSel = settings.buttonStyle.equals(sKey, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onUpdateSettings(settings.copy(buttonStyle = sKey)) }
                                    .testTag("style_btn_$sKey"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) FrostedPrimary else FrostedTintLight,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) FrostedPrimary else FrostedBorder
                                )
                            ) {
                                Text(
                                    text = sLabel,
                                    color = if (isSel) Color.White else FrostedTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 3. Select Layout (4 Layouts)
                Text(
                    text = "چینش دکمه‌ها:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val layouts = listOf(
                    "BOTTOM_SIDES" to "پایین دو طرف",
                    "BOTTOM_CENTER" to "وسط پایین",
                    "SCREEN_SIDES" to "دو طرف صفحه",
                    "VERTICAL" to "عمودی"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        layouts.take(2).forEach { (lKey, lLabel) ->
                            val isSel = settings.buttonLayout.equals(lKey, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onUpdateSettings(settings.copy(buttonLayout = lKey)) }
                                    .testTag("layout_btn_$lKey"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) FrostedPrimary else FrostedTintLight,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) FrostedPrimary else FrostedBorder
                                )
                            ) {
                                Text(
                                    text = lLabel,
                                    color = if (isSel) Color.White else FrostedTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        layouts.drop(2).forEach { (lKey, lLabel) ->
                            val isSel = settings.buttonLayout.equals(lKey, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onUpdateSettings(settings.copy(buttonLayout = lKey)) }
                                    .testTag("layout_btn_$lKey"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) FrostedPrimary else FrostedTintLight,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) FrostedPrimary else FrostedBorder
                                )
                            ) {
                                Text(
                                    text = lLabel,
                                    color = if (isSel) Color.White else FrostedTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 4. Select Size (3 Sizes)
                Text(
                    text = "اندازه دکمه‌ها:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "SMALL" to "کوچک",
                        "MEDIUM" to "متوسط",
                        "LARGE" to "بزرگ"
                    ).forEach { (sizeKey, sizeLabel) ->
                        val isSel = settings.buttonSize.equals(sizeKey, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(buttonSize = sizeKey)) }
                                .testTag("size_btn_$sizeKey"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) FrostedPrimary else FrostedTintLight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) FrostedPrimary else FrostedBorder
                            )
                        ) {
                            Text(
                                text = sizeLabel,
                                color = if (isSel) Color.White else FrostedTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 9.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 5. Emojis (Answer & Reject)
                val emojiPresets = listOf("📞", "💚", "❤️", "😎", "✨", "🔥", "❌", "🚫", "👋")

                Text(
                    text = "ایموجی دکمه پاسخ:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    emojiPresets.take(5).forEach { em ->
                        val isSel = settings.answerEmoji == em
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(answerEmoji = em)) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) CallinoSuccess.copy(alpha = 0.25f) else FrostedTintLight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) CallinoSuccess else FrostedBorder
                            )
                        ) {
                            Text(
                                text = em,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "ایموجی دکمه رد تماس:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    emojiPresets.takeLast(5).forEach { em ->
                        val isSel = settings.rejectEmoji == em
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(rejectEmoji = em)) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) CallinoDanger.copy(alpha = 0.25f) else FrostedTintLight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) CallinoDanger else FrostedBorder
                            )
                        ) {
                            Text(
                                text = em,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 6. Text Customization (Presets & Custom Input)
                Text(
                    text = "متن دکمه پاسخ:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("پاسخ", "جواب میدم", "بفرمایید").forEach { pText ->
                        val isSel = settings.answerText == pText
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(answerText = pText)) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) FrostedPrimary else FrostedTintLight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) FrostedPrimary else FrostedBorder
                            )
                        ) {
                            Text(
                                text = pText,
                                color = if (isSel) Color.White else FrostedTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 7.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = settings.answerText,
                    onValueChange = { onUpdateSettings(settings.copy(answerText = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("متن دلخواه پاسخ") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FrostedPrimary,
                        unfocusedBorderColor = FrostedBorder
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "متن دکمه رد تماس:",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("رد تماس", "بعداً تماس میگیرم", "مشغولم").forEach { rText ->
                        val isSel = settings.rejectText == rText
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(rejectText = rText)) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) Color(0xFFEF4444) else FrostedTintLight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) Color(0xFFEF4444) else FrostedBorder
                            )
                        ) {
                            Text(
                                text = rText,
                                color = if (isSel) Color.White else FrostedTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 7.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = settings.rejectText,
                    onValueChange = { onUpdateSettings(settings.copy(rejectText = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("متن دلخواه رد تماس") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFEF4444),
                        unfocusedBorderColor = FrostedBorder
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Visual Effects Group (Dim, Blur, Glow, Particles, Animations)
        item {
            SettingsSectionCard(title = "افکت‌های بصری و پس‌زمینه", icon = Icons.Default.Opacity) {
                SettingSwitchRow(
                    title = "انیمیشن‌های زنده و امواج",
                    subtitle = "پخش افکت‌های موج و نبض نوری در تماس",
                    checked = settings.animationsEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(animationsEnabled = it)) },
                    tag = "toggle_animations"
                )

                Spacer(modifier = Modifier.height(14.dp))

                SettingSwitchRow(
                    title = "ذرات معلق نورانی (Particles)",
                    subtitle = "نمایش ذرات نورانی و درخشان شناور در صفحه تماس",
                    checked = settings.enableParticles,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableParticles = it)) },
                    tag = "toggle_particles"
                )

                Spacer(modifier = Modifier.height(14.dp))

                SettingSwitchRow(
                    title = "هاله نورانی (Glow Effect)",
                    subtitle = "درخشش نئونی و نور رنگی دور دکمه‌های پاسخ و رد تماس",
                    checked = settings.enableGlow,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableGlow = it)) },
                    tag = "toggle_glow"
                )

                Spacer(modifier = Modifier.height(14.dp))

                SettingSwitchRow(
                    title = "مات‌کردن تصویر یا ویدئو (Blur)",
                    subtitle = "اعمال افکت شیشه‌ای و محو روی پس‌زمینه",
                    checked = settings.enableBlur,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableBlur = it)) },
                    tag = "toggle_blur"
                )

                if (settings.enableBlur) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "شدت مات‌سازی: ${settings.blurAmount.toInt()} dp",
                        color = FrostedTextPrimary,
                        fontSize = 13.sp
                    )
                    Slider(
                        value = settings.blurAmount,
                        onValueChange = { onUpdateSettings(settings.copy(blurAmount = it)) },
                        valueRange = 4f..30f,
                        colors = SliderDefaults.colors(
                            thumbColor = FrostedPrimary,
                            activeTrackColor = FrostedPrimary,
                            inactiveTrackColor = FrostedBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("blur_amount_slider")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dim Level Slider
                Text(
                    text = "میزان تاریکی پس‌زمینه (Dim): ${(settings.backgroundDim * 100).toInt()}٪",
                    color = FrostedTextPrimary,
                    fontSize = 13.sp
                )
                Slider(
                    value = settings.backgroundDim,
                    onValueChange = { onUpdateSettings(settings.copy(backgroundDim = it)) },
                    valueRange = 0.0f..0.8f,
                    colors = SliderDefaults.colors(
                        thumbColor = FrostedPrimary,
                        activeTrackColor = FrostedPrimary,
                        inactiveTrackColor = FrostedBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("dim_slider")
                )
            }
        }

        // System & Permissions Shortcut
        item {
            SettingsSectionCard(title = "دسترسی‌ها و عملکرد", icon = Icons.Default.Security) {
                SettingSwitchRow(
                    title = "بازخورد لمسی دکمه‌ها (Haptic Feedback)",
                    subtitle = "لرزش کوتاه هنگام لمس دکمه‌های پاسخ و رد تماس",
                    checked = settings.enableHaptic,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableHaptic = it)) },
                    tag = "toggle_haptic"
                )

                Spacer(modifier = Modifier.height(14.dp))

                SettingSwitchRow(
                    title = "لرزش (ویبره) هنگام تماس",
                    subtitle = "هماهنگ با ریتم تماس ورودی",
                    checked = settings.vibrateOnCall,
                    onCheckedChange = { onUpdateSettings(settings.copy(vibrateOnCall = it)) },
                    tag = "toggle_vibrate"
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onNavigateToGuide,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedTint, contentColor = FrostedPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("مشاهده و بررسی وضعیت دسترسی‌ها", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Reset Settings Button with Confirmation Dialog
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF4444))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بازگردانی تنظیمات به حالت اولیه",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "تمام تنظیمات شخصی‌سازی به مقادیر اولیه کارخانه باز خواهند گشت.",
                        style = MaterialTheme.typography.bodySmall,
                        color = FrostedTextSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("reset_settings_btn"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("بازگردانی تنظیمات", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Confirmation Dialog for Reset Settings
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "آیا مطمئن هستید؟",
                    color = FrostedTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "تنظیمات شخصی‌سازی‌شده به حالت پیش‌فرض بازگردانده خواهند شد.",
                    color = FrostedTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetSettings()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("بله، بازگردانی شود", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("انصراف", color = FrostedTextSecondary)
                }
            },
            containerColor = FrostedGlassSolid,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(FrostedTintLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = FrostedPrimary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = FrostedTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = FrostedTextSecondary, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = FrostedPrimary,
                uncheckedThumbColor = FrostedTextSecondary,
                uncheckedTrackColor = FrostedTintLight
            ),
            modifier = Modifier.testTag(tag)
        )
    }
}
