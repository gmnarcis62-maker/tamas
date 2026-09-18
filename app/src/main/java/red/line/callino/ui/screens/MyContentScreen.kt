package red.line.callino.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WorkspacePremium
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.data.UserMedia
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
fun MyContentScreen(
    userMediaList: List<UserMedia>,
    vipStatus: VipStatus = VipStatus(),
    onAddUserMedia: (title: String, uri: String, type: ThemeType) -> Unit,
    onDeleteUserMedia: (id: String) -> Unit,
    onSetAsActiveCallTheme: (UserMedia) -> Unit,
    onPreviewUserMedia: (UserMedia) -> Unit,
    onNavigateToVip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showNameDialog by remember { mutableStateOf(false) }
    var showVipVideoDialog by remember { mutableStateOf(false) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }
    var pendingType by remember { mutableStateOf(ThemeType.IMAGE) }
    var customTitle by remember { mutableStateOf("") }

    // Media Pickers
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            pendingUri = uri
            pendingType = ThemeType.IMAGE
            customTitle = "عکس من ${userMediaList.size + 1}"
            showNameDialog = true
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            if (!vipStatus.isVip) {
                // Gated for VIP
                showVipVideoDialog = true
            } else {
                pendingUri = uri
                pendingType = ThemeType.VIDEO
                customTitle = "ویدیوی من ${userMediaList.size + 1}"
                showNameDialog = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .testTag("my_content_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "📁 محتوای من",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "افزودن عکس‌ها و ویدیوهای شخصی از حافظه گوشی برای پس‌زمینه تماس",
                    style = MaterialTheme.typography.bodySmall,
                    color = FrostedTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons to Add Media
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f).height(48.dp).testTag("add_photo_btn"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrostedPrimary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("افزودن عکس", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        videoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    },
                    modifier = Modifier.weight(1f).height(48.dp).testTag("add_video_btn"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrostedTint,
                        contentColor = FrostedPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Icon(Icons.Default.VideoFile, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("افزودن ویدئو", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (userMediaList.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = FrostedTextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "هنوز هیچ محتوایی اضافه نشده است.",
                            color = FrostedTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "عکس یا ویدیوی دلخواه خود را از گالری اضافه کنید تا برای تماس‌ها استفاده شود.",
                            color = FrostedTextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(userMediaList) { media ->
                        UserMediaCard(
                            media = media,
                            onSetAsActive = { onSetAsActiveCallTheme(media) },
                            onPreview = { onPreviewUserMedia(media) },
                            onDelete = { onDeleteUserMedia(media.id) }
                        )
                    }
                }
            }
        }

        // Dialog for entering custom title
        if (showNameDialog && pendingUri != null) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = {
                    Text(
                        text = "نام‌گذاری محتوای جدید",
                        color = FrostedTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "برای فایل انتخاب‌شده یک عنوان دلخواه بنویسید:",
                            color = FrostedTextSecondary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = customTitle,
                            onValueChange = { customTitle = it },
                            label = { Text("عنوان فایل") },
                            modifier = Modifier.fillMaxWidth().testTag("custom_title_input"),
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
                            if (customTitle.isNotBlank()) {
                                onAddUserMedia(customTitle.trim(), pendingUri.toString(), pendingType)
                                showNameDialog = false
                                pendingUri = null
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FrostedPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("ذخیره در محتوای من", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNameDialog = false }) {
                        Text("انصراف", color = FrostedTextSecondary)
                    }
                },
                containerColor = FrostedGlassSolid,
                shape = RoundedCornerShape(24.dp)
            )
        }

        // VIP Video Gating Dialog
        if (showVipVideoDialog) {
            AlertDialog(
                onDismissRequest = { showVipVideoDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "قابلیت ویژه: ویدیوی شخصی تماس",
                            color = FrostedTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "استفاده از ویدیوهای شخصی به عنوان پس‌زمینه زنده تماس ورودی، از امکانات اشتراک ویژه تماسینو پلاس است.",
                            color = FrostedTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "عکس‌های شخصی به صورت کاملاً رایگان قابل استفاده هستند. برای بازگشایی ویدیوها و تمام تم‌های نئونی، اشتراک VIP را تهیه کنید.",
                            color = FrostedTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 20.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showVipVideoDialog = false
                            onNavigateToVip()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB800),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("مشاهده و خرید اشتراک VIP", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showVipVideoDialog = false }) {
                        Text("متوجه شدم", color = FrostedTextSecondary)
                    }
                },
                containerColor = FrostedGlassSolid,
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Composable
private fun UserMediaCard(
    media: UserMedia,
    onSetAsActive: () -> Unit,
    onPreview: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("user_media_card_${media.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Media thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.DarkGray)
            ) {
                AsyncImage(
                    model = media.uri,
                    contentDescription = media.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = media.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (media.mediaType == ThemeType.VIDEO) Icons.Default.VideoLibrary else Icons.Default.Image,
                        contentDescription = null,
                        tint = FrostedSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (media.mediaType == ThemeType.VIDEO) "ویدئوی شخصی" else "تصویر شخصی",
                        style = MaterialTheme.typography.bodySmall,
                        color = FrostedTextSecondary
                    )
                }
            }

            // Quick Actions
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = onPreview,
                    modifier = Modifier
                        .background(FrostedTintLight, CircleShape)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "پیش‌نمایش",
                        tint = FrostedPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onSetAsActive,
                    modifier = Modifier
                        .background(FrostedTintDeep, CircleShape)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "تنظیم به عنوان تم فعال",
                        tint = FrostedPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .background(Color(0xFFFEE2E2), CircleShape)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
