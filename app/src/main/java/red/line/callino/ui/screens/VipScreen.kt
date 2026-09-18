package red.line.callino.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.billing.VipTier
import red.line.callino.data.VipStatus
import red.line.callino.ui.theme.FrostedBorder
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
fun VipScreen(
    vipStatus: VipStatus,
    onPurchaseTier: (VipTier) -> Unit,
    onApplyPromoCode: (String) -> Unit,
    onRestorePurchases: () -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTier by remember { mutableStateOf(VipTier.THREE_MONTHS) }
    var promoCodeInput by remember { mutableStateOf("") }
    var showPromoDialog by remember { mutableStateOf(false) }

    val goldBrush = Brush.linearGradient(
        listOf(
            Color(0xFFFFD700),
            Color(0xFFFFA500),
            Color(0xFFFF8C00)
        )
    )

    val vipCardBg = Brush.linearGradient(
        listOf(
            Color(0xFF2A1B4E),
            Color(0xFF1B1530)
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Gold Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vip_hero_card"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.5.dp, Color(0xFFFFD700).copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(vipCardBg)
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(goldBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "تماسینو پلاس (VIP)",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (vipStatus.isVip) "اشتراک VIP شما فعال است ✨" else "دسترسی نامحدود به تمامی تم‌ها و قابلیت‌های پیشرفته",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )

                        if (vipStatus.isVip) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = Color(0xFFFFD700).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFFFD700))
                            ) {
                                Text(
                                    text = "نوع اشتراک: ${vipStatus.vipType}",
                                    color = Color(0xFFFFD700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Features Checklist
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = BorderStroke(1.dp, FrostedBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "امکانات ویژه اشتراک VIP:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FrostedTextPrimary
                    )

                    VipFeatureRow(
                        title = "پخش ویدیوهای تمام‌صفحه برای تماس ورودی",
                        subtitle = "امکان استفاده از فایل‌های ویدیویی زنده",
                        icon = Icons.Default.VideoLibrary
                    )

                    VipFeatureRow(
                        title = "دسترسی به تمامی پوسته‌ها و تم‌های نئونی",
                        subtitle = "پوسته‌های اختصاصی کهکشان، سایبرپانک و رمانتیک",
                        icon = Icons.Default.AutoAwesome
                    )

                    VipFeatureRow(
                        title = "تم اختصاصی برای هر مخاطب بدون محدودیت",
                        subtitle = "شخصی‌سازی جداگانه برای دوستان و خانواده",
                        icon = Icons.Default.Loyalty
                    )

                    VipFeatureRow(
                        title = "افکت‌های نوری، مات‌سازی شیشه‌ای و ذرات معلق",
                        subtitle = "افکت‌های بصری لوکس Glow و Particles",
                        icon = Icons.Default.Diamond
                    )

                    VipFeatureRow(
                        title = "تجربه کاملاً بدون تبلیغات و پشتیبانی ویژه",
                        subtitle = "استفاده روان و نامحدود در تمام بخش‌ها",
                        icon = Icons.Default.CheckCircle
                    )
                }
            }
        }

        // Tier Selection Cards
        if (!vipStatus.isVip) {
            item {
                Text(
                    text = "طرح‌های اشتراک تماسینو:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
            }

            items(VipTier.values()) { tier ->
                val isSelected = selectedTier == tier

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { selectedTier = tier }
                        .testTag("vip_tier_card_${tier.id}"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) FrostedTintLight else FrostedGlassSolid
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(0xFFFFD700) else FrostedBorder
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(
                                        2.dp,
                                        if (isSelected) Color(0xFFFFD700) else FrostedBorder,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFD700))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = tier.titleFa,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = FrostedTextPrimary
                                )
                                if (tier == VipTier.THREE_MONTHS) {
                                    Text(
                                        text = "🔥 پرطرفدارترین انتخاب کاربران",
                                        fontSize = 11.sp,
                                        color = Color(0xFFE11D48),
                                        fontWeight = FontWeight.Bold
                                    )
                                } else if (tier == VipTier.LIFETIME) {
                                    Text(
                                        text = "💎 پرداخت یکباره برای همیشه",
                                        fontSize = 11.sp,
                                        color = Color(0xFF059669),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = tier.priceFa,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) FrostedPrimary else FrostedTextPrimary
                        )
                    }
                }
            }

            // Purchase / Action Button
            item {
                Button(
                    onClick = { onPurchaseTier(selectedTier) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("vip_purchase_btn"),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB800),
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "فعال‌سازی ${selectedTier.titleFa} (${selectedTier.priceFa})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Promo Code & Restore Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { showPromoDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("open_promo_dialog_btn"),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrostedTint,
                        contentColor = FrostedPrimary
                    ),
                    border = BorderStroke(1.dp, FrostedBorder)
                ) {
                    Icon(Icons.Default.Discount, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("کد هدیه / تخفیف", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onRestorePurchases() },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("restore_purchases_btn"),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrostedTintLight,
                        contentColor = FrostedTextPrimary
                    ),
                    border = BorderStroke(1.dp, FrostedBorder)
                ) {
                    Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("بازیابی خرید", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Promo Code Dialog
    if (showPromoDialog) {
        AlertDialog(
            onDismissRequest = { showPromoDialog = false },
            title = {
                Text(
                    text = "🎁 فعال‌سازی با کد هدیه یا معرف",
                    color = FrostedTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "اگر کد اشتراک ویژه یا کد تخفیف دارید، آن را در کادر زیر وارد کنید:",
                        color = FrostedTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = promoCodeInput,
                        onValueChange = { promoCodeInput = it },
                        label = { Text("کد هدیه (مثال: CALLINO_VIP)") },
                        modifier = Modifier.fillMaxWidth().testTag("promo_code_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FrostedPrimary,
                            unfocusedBorderColor = FrostedBorder,
                            focusedTextColor = FrostedTextPrimary,
                            unfocusedTextColor = FrostedTextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (promoCodeInput.isNotBlank()) {
                            onApplyPromoCode(promoCodeInput.trim())
                            showPromoDialog = false
                            promoCodeInput = ""
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrostedPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("ثبت و بررسی کد", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPromoDialog = false }) {
                    Text("انصراف", color = FrostedTextSecondary)
                }
            },
            containerColor = FrostedGlassSolid,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun VipFeatureRow(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(FrostedTintDeep),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FrostedPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = FrostedTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = FrostedTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
