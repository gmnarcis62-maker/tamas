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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AppSettings
import red.line.callino.data.EffectSelectionMode
import red.line.callino.data.EffectType
import red.line.callino.data.EffectsCatalog
import red.line.callino.ui.components.CallButtonsContainer
import red.line.callino.ui.components.EffectPickerDialog
import red.line.callino.ui.theme.Radius
import red.line.callino.ui.theme.Spacing

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdateSettings: (AppSettings) -> Unit,
    onResetSettings: () -> Unit,
    onNavigateToGuide: () -> Unit,
    onOpenEffectsGallery: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showGlobalEffectPicker by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.screenHorizontal),
        contentPadding = PaddingValues(top = Spacing.lg, bottom = Spacing.screenBottom),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        item {
            Column {
                Text(
                    text = "تنظیمات تماسینو",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "شخصی‌سازی کامل صفحه تماس‌های ورودی",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            SettingsSectionCard(title = "سرویس تماسینو", icon = Icons.Default.PhoneInTalk) {
                SettingSwitchRow(
                    title = "فعال بودن سرویس",
                    subtitle = if (settings.isServiceEnabled)
                        "صفحه اختصاصی تماسینو هنگام تماس‌های ورودی نمایش داده می‌شود"
                    else
                        "برای نمایش تم‌ها روی صفحه تماس، سرویس را فعال کنید",
                    checked = settings.isServiceEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(isServiceEnabled = it)) },
                    tag = "toggle_service_master"
                )
            }
        }

        // ---- افکت‌های پریمیوم ----
        item {
            SettingsSectionCard(title = "افکت‌های پریمیوم", icon = Icons.Default.AutoAwesome) {
                Text(
                    text = "حالت انتخاب افکت روی تم‌ها:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(Spacing.sm))

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    EffectModeOption(
                        mode = EffectSelectionMode.RANDOM,
                        isSelected = settings.effectSelectionMode == EffectSelectionMode.RANDOM,
                        onClick = { onUpdateSettings(settings.copy(effectSelectionMode = EffectSelectionMode.RANDOM)) }
                    )
                    EffectModeOption(
                        mode = EffectSelectionMode.MANUAL,
                        isSelected = settings.effectSelectionMode == EffectSelectionMode.MANUAL,
                        onClick = { onUpdateSettings(settings.copy(effectSelectionMode = EffectSelectionMode.MANUAL)) }
                    )
                    EffectModeOption(
                        mode = EffectSelectionMode.GLOBAL,
                        isSelected = settings.effectSelectionMode == EffectSelectionMode.GLOBAL,
                        onClick = { onUpdateSettings(settings.copy(effectSelectionMode = EffectSelectionMode.GLOBAL)) }
                    )
                    EffectModeOption(
                        mode = EffectSelectionMode.OFF,
                        isSelected = settings.effectSelectionMode == EffectSelectionMode.OFF,
                        onClick = { onUpdateSettings(settings.copy(effectSelectionMode = EffectSelectionMode.OFF)) }
                    )
                }

                if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL ||
                    settings.effectSelectionMode == EffectSelectionMode.MANUAL) {
                    Spacer(Modifier.height(Spacing.lg))

                    val currentEffect = if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL) {
                        settings.globalEffect
                    } else {
                        EffectType.MESH_GRADIENT
                    }

                    Surface(
                        shape = RoundedCornerShape(Radius.md),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL) {
                                    showGlobalEffectPicker = true
                                } else {
                                    onOpenEffectsGallery()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.md),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL)
                                        "افکت فعلی سراسری:"
                                    else
                                        "انتخاب افکت برای هر تم:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL)
                                        currentEffect.label
                                    else
                                        "ورود به گالری",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (settings.effectSelectionMode == EffectSelectionMode.GLOBAL)
                                            Icons.Default.Palette
                                        else Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(Spacing.md))

                Button(
                    onClick = onOpenEffectsGallery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("open_effects_gallery_btn"),
                    shape = RoundedCornerShape(Radius.sm),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "مشاهده گالری ${EffectsCatalog.all.size} افکت",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // ---- اطلاعات نمایش تماس ----
        item {
            SettingsSectionCard(title = "اطلاعات نمایش تماس", icon = Icons.Default.Visibility) {
                SettingSwitchRow(
                    title = "نمایش نام مخاطب",
                    subtitle = "نام مخاطب روی صفحه تماس نمایش داده شود",
                    checked = settings.callerNameVisible,
                    onCheckedChange = { onUpdateSettings(settings.copy(callerNameVisible = it)) },
                    tag = "toggle_caller_name"
                )
                Spacer(Modifier.height(Spacing.md))
                SettingSwitchRow(
                    title = "نمایش شماره تماس",
                    subtitle = "شماره تماس روی صفحه تماس نمایش داده شود",
                    checked = settings.callerNumberVisible,
                    onCheckedChange = { onUpdateSettings(settings.copy(callerNumberVisible = it)) },
                    tag = "toggle_caller_number"
                )
                Spacer(Modifier.height(Spacing.lg))
                Text(
                    text = "موقعیت اطلاعات تماس:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(Spacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    listOf(
                        "TOP" to "بالا",
                        "CENTER" to "مرکز",
                        "BOTTOM" to "پایین"
                    ).forEach { (posKey, posLabel) ->
                        val isSel = settings.callerInfoPosition == posKey
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(callerInfoPosition = posKey)) },
                            shape = RoundedCornerShape(Radius.sm),
                            color = if (isSel) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text(
                                text = posLabel,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(Modifier.height(Spacing.lg))
                Text(
                    text = "اندازه قلم: ${(settings.fontSizeScale * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
                Slider(
                    value = settings.fontSizeScale,
                    onValueChange = { onUpdateSettings(settings.copy(fontSizeScale = it)) },
                    valueRange = 0.8f..1.3f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("font_size_slider")
                )
            }
        }

        // ---- طراحی دکمه‌ها ----
        item {
            SettingsSectionCard(title = "طراحی دکمه‌های تماس", icon = Icons.Default.Palette) {
                Text(
                    text = "پیش‌نمایش زنده دکمه‌ها:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(Spacing.sm))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(Radius.lg))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                            )
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(Radius.lg)
                        )
                        .padding(Spacing.md),
                    contentAlignment = Alignment.Center
                ) {
                    CallButtonsContainer(
                        settings = settings,
                        onAnswerClick = {},
                        onRejectClick = {}
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(Spacing.xs),
                        shape = RoundedCornerShape(Radius.xs),
                        color = Color.Black.copy(alpha = 0.55f)
                    ) {
                        Text(
                            text = "پیش‌نمایش زنده",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(Modifier.height(Spacing.xl))

                Text(
                    text = "استایل دکمه‌ها:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(Spacing.sm))

                val styles = listOf(
                    "GLASS" to "شیشه‌ای",
                    "CRYSTAL" to "کریستال",
                    "NEON" to "نئون",
                    "ROUNDED" to "گرد",
                    "PILL" to "کپسولی",
                    "MINIMAL" to "مینیمال"
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        styles.take(3).forEach { (sKey, sLabel) ->
                            StyleOption(
                                label = sLabel,
                                isSelected = settings.buttonStyle.equals(sKey, ignoreCase = true),
                                onClick = { onUpdateSettings(settings.copy(buttonStyle = sKey)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        styles.drop(3).forEach { (sKey, sLabel) ->
                            StyleOption(
                                label = sLabel,
                                isSelected = settings.buttonStyle.equals(sKey, ignoreCase = true),
                                onClick = { onUpdateSettings(settings.copy(buttonStyle = sKey)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Spacing.xl))

                Text(
                    text = "چیدمان دکمه‌ها:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(Spacing.sm))

                val layouts = listOf(
                    "BOTTOM_SIDES" to "پایین - چپ و راست",
                    "BOTTOM_CENTER" to "پایین - کنار هم",
                    "SCREEN_SIDES" to "دو طرف صفحه",
                    "VERTICAL" to "عمودی"
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        layouts.take(2).forEach { (lKey, lLabel) ->
                            StyleOption(
                                label = lLabel,
                                isSelected = settings.buttonLayout.equals(lKey, ignoreCase = true),
                                onClick = { onUpdateSettings(settings.copy(buttonLayout = lKey)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        layouts.drop(2).forEach { (lKey, lLabel) ->
                            StyleOption(
                                label = lLabel,
                                isSelected = settings.buttonLayout.equals(lKey, ignoreCase = true),
                                onClick = { onUpdateSettings(settings.copy(buttonLayout = lKey)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Spacing.xl))

                Text(
                    text = "اندازه دکمه‌ها:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(Spacing.sm))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    listOf(
                        "SMALL" to "کوچک",
                        "MEDIUM" to "متوسط",
                        "LARGE" to "بزرگ"
                    ).forEach { (sizeKey, sizeLabel) ->
                        StyleOption(
                            label = sizeLabel,
                            isSelected = settings.buttonSize.equals(sizeKey, ignoreCase = true),
                            onClick = { onUpdateSettings(settings.copy(buttonSize = sizeKey)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(Spacing.xl))

                Text(
                    text = "آیکون دکمه پاسخ:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(Spacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    listOf("📞", "✔️", "☎️", "💚", "🤙").forEach { em ->
                        val isSel = settings.answerEmoji == em
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(answerEmoji = em)) },
                            shape = RoundedCornerShape(Radius.xs),
                            color = if (isSel) Color(0xFF10B981).copy(alpha = 0.25f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) Color(0xFF10B981) else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text(
                                text = em,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Spacing.md))

                Text(
                    text = "آیکون دکمه رد تماس:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(Spacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    listOf("❌", "🚫", "✖️", "📵", "🔕").forEach { em ->
                        val isSel = settings.rejectEmoji == em
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onUpdateSettings(settings.copy(rejectEmoji = em)) },
                            shape = RoundedCornerShape(Radius.xs),
                            color = if (isSel) Color(0xFFEF4444).copy(alpha = 0.25f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) Color(0xFFEF4444) else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text(
                                text = em,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Spacing.xl))

                Text(
                    text = "متن دکمه پاسخ:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(Spacing.xs))
                OutlinedTextField(
                    value = settings.answerText,
                    onValueChange = { onUpdateSettings(settings.copy(answerText = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(Radius.sm)
                )

                Spacer(Modifier.height(Spacing.md))

                Text(
                    text = "متن دکمه رد تماس:",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(Spacing.xs))
                OutlinedTextField(
                    value = settings.rejectText,
                    onValueChange = { onUpdateSettings(settings.copy(rejectText = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFEF4444),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(Radius.sm)
                )
            }
        }

        // ---- افکت‌های بصری عمومی ----
        item {
            SettingsSectionCard(title = "افکت‌های بصری عمومی", icon = Icons.Default.Opacity) {
                SettingSwitchRow(
                    title = "انیمیشن‌ها",
                    subtitle = "افکت‌های پویا و متحرک روی صفحه تماس",
                    checked = settings.animationsEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(animationsEnabled = it)) },
                    tag = "toggle_animations"
                )
                Spacer(Modifier.height(Spacing.md))
                SettingSwitchRow(
                    title = "ذرات نورانی شناور",
                    subtitle = "ذرات کوچک نورانی روی پس‌زمینه تماس",
                    checked = settings.enableParticles,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableParticles = it)) },
                    tag = "toggle_particles"
                )
                Spacer(Modifier.height(Spacing.md))
                SettingSwitchRow(
                    title = "افکت درخشش (Glow)",
                    subtitle = "درخشش نرم دور کارت اطلاعات تماس",
                    checked = settings.enableGlow,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableGlow = it)) },
                    tag = "toggle_glow"
                )
                Spacer(Modifier.height(Spacing.md))
                SettingSwitchRow(
                    title = "تاری پس‌زمینه (Blur)",
                    subtitle = "محو کردن پس‌زمینه تماس",
                    checked = settings.enableBlur,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableBlur = it)) },
                    tag = "toggle_blur"
                )
                if (settings.enableBlur) {
                    Spacer(Modifier.height(Spacing.md))
                    Text(
                        text = "مقدار تاری: ${settings.blurAmount.toInt()} dp",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                    Slider(
                        value = settings.blurAmount,
                        onValueChange = { onUpdateSettings(settings.copy(blurAmount = it)) },
                        valueRange = 4f..30f,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.height(Spacing.lg))
                Text(
                    text = "شدت تاری پس‌زمینه: ${(settings.backgroundDim * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp
                )
                Slider(
                    value = settings.backgroundDim,
                    onValueChange = { onUpdateSettings(settings.copy(backgroundDim = it)) },
                    valueRange = 0.0f..0.8f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            SettingsSectionCard(title = "سیستم و لرزش", icon = Icons.Default.Security) {
                SettingSwitchRow(
                    title = "لرزش لمسی (Haptic)",
                    subtitle = "بازخورد لرزشی هنگام لمس دکمه‌ها",
                    checked = settings.enableHaptic,
                    onCheckedChange = { onUpdateSettings(settings.copy(enableHaptic = it)) },
                    tag = "toggle_haptic"
                )
                Spacer(Modifier.height(Spacing.md))
                SettingSwitchRow(
                    title = "لرزش هنگام تماس",
                    subtitle = "لرزش گوشی هنگام دریافت تماس",
                    checked = settings.vibrateOnCall,
                    onCheckedChange = { onUpdateSettings(settings.copy(vibrateOnCall = it)) },
                    tag = "toggle_vibrate"
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Radius.lg),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color(0xFFEF4444).copy(alpha = 0.4f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(Spacing.lg)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFEF4444)
                        )
                        Spacer(Modifier.width(Spacing.sm))
                        Text(
                            text = "بازنشانی تنظیمات",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }
                    Spacer(Modifier.height(Spacing.sm))
                    Text(
                        text = "تمام تنظیمات به حالت پیش‌فرض باز می‌گردد.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(Spacing.lg))
                    Button(
                        onClick = { showResetDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("reset_settings_btn"),
                        shape = RoundedCornerShape(Radius.sm),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(Spacing.sm))
                        Text("بازنشانی تنظیمات", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "تأیید بازنشانی",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "آیا مطمئن هستید که می‌خواهید تمام تنظیمات را به حالت پیش‌فرض بازگردانید؟",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetSettings()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(Radius.sm)
                ) {
                    Text("بله، بازنشانی کن", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("انصراف", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(Radius.lg)
        )
    }

    if (showGlobalEffectPicker) {
        EffectPickerDialog(
            currentEffect = settings.globalEffect,
            onEffectSelected = { effect ->
                onUpdateSettings(settings.copy(globalEffect = effect))
            },
            onDismiss = { showGlobalEffectPicker = false }
        )
    }
}

@Composable
private fun EffectModeOption(
    mode: EffectSelectionMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(Radius.md),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.md))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .border(
                        1.5.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            Spacer(Modifier.width(Spacing.sm))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mode.label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = mode.description,
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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
        shape = RoundedCornerShape(Radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(Spacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(Spacing.md))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(Spacing.lg))
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
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.testTag(tag)
        )
    }
}

@Composable
private fun StyleOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(Radius.sm),
        color = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        )
    ) {
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 10.dp),
            textAlign = TextAlign.Center
        )
    }
}