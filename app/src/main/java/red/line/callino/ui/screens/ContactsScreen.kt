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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Visibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.CallTheme
import red.line.callino.data.ContactTheme
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
fun ContactsScreen(
    contactThemes: List<ContactTheme>,
    allThemes: List<CallTheme>,
    onAssignContactTheme: (contactId: String, name: String, number: String, themeId: String, label: String?) -> Unit,
    onRemoveContactTheme: (contactId: String) -> Unit,
    onSimulateContactCall: (name: String, number: String, theme: CallTheme?) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    // Dialog state
    var newName by remember { mutableStateOf("") }
    var newNumber by remember { mutableStateOf("") }
    var newLabel by remember { mutableStateOf("مادر") }
    var selectedThemeId by remember { mutableStateOf(allThemes.firstOrNull()?.id ?: "theme_aurora") }

    val filteredContacts = contactThemes.filter {
        it.contactName.contains(searchQuery, ignoreCase = true) ||
        it.contactNumber.contains(searchQuery) ||
        (it.relationshipLabel?.contains(searchQuery, ignoreCase = true) == true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .testTag("contacts_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "👤 تم اختصاصی مخاطبین",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "برای هر فرد دلخواه (مادر، دوست، همکار...) تم تماس جداگانه قرار دهید.",
                    style = MaterialTheme.typography.bodySmall,
                    color = FrostedTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search & Add Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("جستجوی نام یا شماره مخاطب...", fontSize = 13.sp, color = FrostedTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = FrostedPrimary) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("contacts_search_input"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FrostedGlassSolid,
                        unfocusedContainerColor = FrostedGlassSolid,
                        focusedBorderColor = FrostedPrimary,
                        unfocusedBorderColor = FrostedBorder,
                        focusedTextColor = FrostedTextPrimary,
                        unfocusedTextColor = FrostedTextPrimary
                    ),
                    singleLine = true
                )

                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.height(52.dp).testTag("add_custom_contact_btn"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedPrimary, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("مخاطب جدید", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredContacts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = FrostedTextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isEmpty()) "هیچ مخاطب شخصی‌سازی‌شده‌ای ثبت نشده است." else "مخاطبی با این مشخصات یافت نشد.",
                            color = FrostedTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredContacts) { contact ->
                        val theme = allThemes.find { it.id == contact.themeId }

                        ContactThemeRowCard(
                            contact = contact,
                            theme = theme,
                            onSimulateCall = {
                                onSimulateContactCall(contact.contactName, contact.contactNumber, theme)
                            },
                            onDelete = {
                                onRemoveContactTheme(contact.contactId)
                            }
                        )
                    }
                }
            }
        }

        // Add Contact Dialog
        if (showAddDialog) {
            val labelOptions = listOf("مادر", "پدر", "همسر", "دوست", "همکار", "خانواده", "سایر")

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Text(
                        text = "اختصاص تم به مخاطب جدید",
                        color = FrostedTextPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("نام مخاطب (مثال: مادر، علی...)") },
                            modifier = Modifier.fillMaxWidth().testTag("dialog_contact_name_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FrostedPrimary,
                                unfocusedBorderColor = FrostedBorder,
                                focusedTextColor = FrostedTextPrimary,
                                unfocusedTextColor = FrostedTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newNumber,
                            onValueChange = { newNumber = it },
                            label = { Text("شماره تماس") },
                            modifier = Modifier.fillMaxWidth().testTag("dialog_contact_phone_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FrostedPrimary,
                                unfocusedBorderColor = FrostedBorder,
                                focusedTextColor = FrostedTextPrimary,
                                unfocusedTextColor = FrostedTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = "برچسب نسبت:", color = FrostedTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(labelOptions) { label ->
                                val isSel = newLabel == label
                                Surface(
                                    modifier = Modifier.clickable { newLabel = label },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSel) FrostedPrimary else FrostedTintLight,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) FrostedPrimary else FrostedBorder)
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSel) Color.White else FrostedTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = "انتخاب تم تماس:", color = FrostedTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(allThemes) { t ->
                                val isSel = selectedThemeId == t.id
                                Surface(
                                    modifier = Modifier.clickable { selectedThemeId = t.id },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSel) FrostedTint else FrostedGlassSolid,
                                    border = androidx.compose.foundation.BorderStroke(if (isSel) 2.dp else 1.dp, if (isSel) FrostedPrimary else FrostedBorder)
                                ) {
                                    Text(
                                        text = t.titleFa,
                                        color = if (isSel) FrostedPrimary else FrostedTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newName.isNotBlank()) {
                                val id = "contact_${System.currentTimeMillis()}"
                                onAssignContactTheme(id, newName.trim(), newNumber.trim(), selectedThemeId, newLabel)
                                showAddDialog = false
                                newName = ""
                                newNumber = ""
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FrostedPrimary, contentColor = Color.White)
                    ) {
                        Text("ذخیره مخاطب", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("انصراف", color = FrostedTextSecondary)
                    }
                },
                containerColor = FrostedGlassSolid,
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Composable
private fun ContactThemeRowCard(
    contact: ContactTheme,
    theme: CallTheme?,
    onSimulateCall: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with relationship initial
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when (contact.relationshipLabel) {
                            "مادر", "همسر" -> FrostedSecondary
                            "دوست" -> FrostedPrimary
                            "همکار" -> Color(0xFF0284C7)
                            else -> FrostedTintDeep
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contact.contactName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FrostedTextPrimary
                    )
                    if (!contact.relationshipLabel.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = FrostedTintLight,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, FrostedBorder)
                        ) {
                            Text(
                                text = contact.relationshipLabel,
                                color = FrostedPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = contact.contactNumber.ifEmpty { "بدون شماره" },
                    style = MaterialTheme.typography.bodySmall,
                    color = FrostedTextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "تم اختصاصی: ${theme?.titleFa ?: "پیش‌فرض تماسینو"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = FrostedPrimary,
                    fontSize = 11.sp
                )
            }

            // Actions
            IconButton(
                onClick = onSimulateCall,
                modifier = Modifier
                    .background(FrostedTintLight, CircleShape)
                    .size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = "تست تماس این مخاطب",
                    tint = FrostedPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .background(Color(0xFFFEE2E2), CircleShape)
                    .size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف تم اختصاصی",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
