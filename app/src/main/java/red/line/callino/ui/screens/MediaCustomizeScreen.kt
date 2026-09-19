package red.line.callino.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import red.line.callino.data.EffectInfo
import red.line.callino.data.EffectType
import red.line.callino.data.EffectsCatalog
import red.line.callino.data.ThemeType
import red.line.callino.data.UserMedia
import red.line.callino.ui.effects.premium.PremiumEffectRenderer
import red.line.callino.ui.theme.Radius
import red.line.callino.ui.theme.Spacing

/**
 * صفحه‌ی شخصی‌سازی کامل یک فایل کاربر.
 * کاربر می‌تواند:
 * - عنوان را تغییر دهد
 * - افکت دلخواه از بین ۲۴ افکت انتخاب کند
 * - شدت افکت را تنظیم کند
 * - تاری پس‌زمینه را تنظیم کند
 * - محو کردن پس‌زمینه را فعال/غیرفعال کند
 * - ذرات شناور و درخشش را فعال/غیرفعال کند
 */
@Composable
fun MediaCustomizeScreen(
    media: UserMedia,
    onSave: (UserMedia) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember(media.id) { mutableStateOf(media.title) }
    var selectedEffect by remember(media.id) { mutableStateOf(media.effect) }
    var intensity by remember(media.id) { mutableFloatStateOf(media.effectIntensity) }
    var dim by remember(media.id) { mutableFloatStateOf(media.backgroundDim) }
    var enableBlur by remember(media.id) { mutableStateOf(media.enableBlur) }
    var blurAmount by remember(media.id) { mutableFloatStateOf(media.blurAmount) }
    var enableParticles by remember(media.id) { mutableStateOf(media.enableParticles) }
    var enableGlow by remember(media.id) { mutableStateOf(media.enableGlow) }
    var showFullPreview by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("media_customize_screen")
        ) {
            // ---------- Top Bar ----------
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("customize_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "بازگشت",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column {
                            Text(
                                text = "شخصی‌سازی محتوا",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "همه‌چیز را دلخواه خودت تغییر بده",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onSave(
                                media.copy(
                                    title = title.trim().ifBlank { media.title },
                                    effect = selectedEffect,
                                    effectIntensity = intensity,
                                    backgroundDim = dim,
                                    enableBlur = enableBlur,
                                    blurAmount = blurAmount,
                                    enableParticles = enableParticles,
                                    enableGlow = enableGlow
                                )
                            )
                        },
                        shape = RoundedCornerShape(Radius.sm),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("customize_save_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("ذخیره", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ---------- Content ----------
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = Spacing.md,
                    bottom = Spacing.xl
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing.lg)
            ) {
                // ---- پیش‌نمایش زنده ----
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "پیش‌نمایش زنده",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            IconButton(
                                onClick = { showFullPreview = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "تمام‌صفحه",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(Modifier.height(Spacing.sm))

                        LiveMediaPreview(
                            media = media,
                            effect = selectedEffect,
                            intensity = intensity,
                            dim = dim,
                            enableBlur = enableBlur,
                            blurAmount = blurAmount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(9f / 14f)
                                .clip(RoundedCornerShape(Radius.lg))
                        )
                    }
                }

                // ---- عنوان ----
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                        Text(
                            text = "عنوان محتوا",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(Spacing.sm))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customize_title_input"),
                            singleLine = true,
                            placeholder = { Text("مثلاً: عکس ساحل") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(Radius.sm)
                        )
                    }
                }

                // ---- انتخاب افکت ----
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.width(Spacing.sm))
                            Column {
                                Text(
                                    text = "افکت روی محتوا",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "از بین ${EffectsCatalog.all.size} افکت یکی را انتخاب کن",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(Modifier.height(Spacing.sm))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = Spacing.md),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            // افکت NONE
                            item {
                                NoEffectChip(
                                    isSelected = selectedEffect == EffectType.NONE,
                                    onClick = { selectedEffect = EffectType.NONE }
                                )
                            }
                            items(EffectsCatalog.all) { info ->
                                EffectMiniChip(
                                    info = info,
                                    isSelected = selectedEffect == info.type,
                                    onClick = { selectedEffect = info.type }
                                )
                            }
                        }
                    }
                }

                // ---- تنظیمات ظاهری ----
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.md),
                        shape = RoundedCornerShape(Radius.lg),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(modifier = Modifier.padding(Spacing.lg)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Tune,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(Spacing.sm))
                                Text(
                                    text = "تنظیمات ظاهری",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(Modifier.height(Spacing.lg))

                            // شدت افکت
                            LabeledSlider(
                                label = "شدت افکت",
                                valueText = "${(intensity * 100).toInt()}%",
                                value = intensity,
                                onValueChange = { intensity = it },
                                valueRange = 0.3f..1.5f
                            )

                            Spacer(Modifier.height(Spacing.md))

                            // تاری
                            LabeledSlider(
                                label = "تاری پس‌زمینه",
                                valueText = "${(dim * 100).toInt()}%",
                                value = dim,
                                onValueChange = { dim = it },
                                valueRange = 0f..0.8f
                            )

                            Spacer(Modifier.height(Spacing.lg))

                            // محو کردن
                            SettingSwitchRow(
                                title = "محو کردن پس‌زمینه",
                                subtitle = "پس‌زمینه محو شود تا اطلاعات مخاطب واضح‌تر دیده شود",
                                checked = enableBlur,
                                onCheckedChange = { enableBlur = it }
                            )

                            if (enableBlur) {
                                Spacer(Modifier.height(Spacing.md))
                                LabeledSlider(
                                    label = "مقدار محو",
                                    valueText = "${blurAmount.toInt()} dp",
                                    value = blurAmount,
                                    onValueChange = { blurAmount = it },
                                    valueRange = 4f..30f
                                )
                            }

                            Spacer(Modifier.height(Spacing.lg))

                            SettingSwitchRow(
                                title = "ذرات شناور",
                                subtitle = "ذرات نورانی کوچک روی پس‌زمینه",
                                checked = enableParticles,
                                onCheckedChange = { enableParticles = it }
                            )

                            Spacer(Modifier.height(Spacing.md))

                            SettingSwitchRow(
                                title = "درخشش (Glow)",
                                subtitle = "درخشش نرم اطراف کارت اطلاعات",
                                checked = enableGlow,
                                onCheckedChange = { enableGlow = it }
                            )
                        }
                    }
                }

                // ---- دکمه ذخیره بزرگ ----
                item {
                    Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                        Button(
                            onClick = {
                                onSave(
                                    media.copy(
                                        title = title.trim().ifBlank { media.title },
                                        effect = selectedEffect,
                                        effectIntensity = intensity,
                                        backgroundDim = dim,
                                        enableBlur = enableBlur,
                                        blurAmount = blurAmount,
                                        enableParticles = enableParticles,
                                        enableGlow = enableGlow
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("customize_save_big_btn"),
                            shape = RoundedCornerShape(Radius.md),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(Spacing.sm))
                            Text(
                                text = "ذخیره تنظیمات",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // ---- پیش‌نمایش تمام‌صفحه ----
    if (showFullPreview) {
        Dialog(onDismissRequest = { showFullPreview = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(Radius.lg))
                    .background(Color.Black)
            ) {
                LiveMediaPreview(
                    media = media,
                    effect = selectedEffect,
                    intensity = intensity,
                    dim = dim,
                    enableBlur = enableBlur,
                    blurAmount = blurAmount,
                    modifier = Modifier.fillMaxSize()
                )
                // دکمه بستن
                IconButton(
                    onClick = { showFullPreview = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Spacing.sm)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "بستن",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// =====================================================
// Components
// =====================================================

@Composable
private fun LiveMediaPreview(
    media: UserMedia,
    effect: EffectType,
    intensity: Float,
    dim: Float,
    enableBlur: Boolean,
    blurAmount: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .background(Color.Black)
    ) {
        // ---- لایه ۱: تصویر یا ویدیو ----
        val imageModifier = if (enableBlur && blurAmount > 0f) {
            Modifier
                .fillMaxSize()
                .blur(blurAmount.dp)
        } else {
            Modifier.fillMaxSize()
        }

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(media.uri)
                .crossfade(true)
                .build(),
            contentDescription = media.title,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )

        // ---- لایه ۲: تاری ----
        if (dim > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = dim))
            )
        }

        // ---- لایه ۳: افکت ----
        if (effect != EffectType.NONE) {
            PremiumEffectRenderer(
                effect = effect,
                animationsEnabled = true,
                intensity = intensity,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ---- بج بالای تصویر ----
        Surface(
            color = Color.Black.copy(alpha = 0.55f),
            shape = RoundedCornerShape(Radius.xs),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(Spacing.sm)
        ) {
            Text(
                text = if (effect == EffectType.NONE) "بدون افکت" else effect.label,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        // ---- نوشته‌ی نمونه در وسط ----
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👤", fontSize = 28.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = media.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "۰۹۱۲ ۳۴۵ ۶۷۸۹",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun EffectMiniChip(
    info: EffectInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 90.dp, height = 120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(Radius.md),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF05050A)),
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PremiumEffectRenderer(
                effect = info.type,
                animationsEnabled = true,
                intensity = 0.85f,
                modifier = Modifier.fillMaxSize()
            )

            // گرادیانت پایین
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(18.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
            ) {
                Text(text = info.icon, fontSize = 14.sp)
                Text(
                    text = info.type.label,
                    color = Color.White,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    lineHeight = 11.sp
                )
            }
        }
    }
}

@Composable
private fun NoEffectChip(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 90.dp, height = 120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(Radius.md),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F1A)),
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // پس‌زمینه‌ی ساده با یک الگوی نازک
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF0F0F1A)
                            )
                        )
                    )
            )

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(18.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
            ) {
                Text(text = "🚫", fontSize = 14.sp)
                Text(
                    text = "بدون افکت",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LabeledSlider(
    label: String,
    valueText: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = valueText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.5.sp
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
            )
        )
    }
}