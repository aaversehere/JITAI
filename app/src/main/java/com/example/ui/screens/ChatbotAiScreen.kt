package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ResearchData
import com.example.ui.theme.EmergencyRed
import com.example.viewmodel.AppUiState
import com.example.viewmodel.ChatMessage
import com.example.viewmodel.MessageSender

@Composable
fun ChatbotAiScreen(
    uiState: AppUiState,
    onSendMessage: (String) -> Unit,
    onNavigateToModul: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Proteksi Kelompok Kontrol (X = 0)
    if (uiState.clusterX == 0 && !uiState.isWaitlistCrossoverUnlocked && !uiState.isPosttestCompleted) {
        ControlGroupLockedView(onNavigateToModul = onNavigateToModul, modifier = modifier)
        return
    }

    var textInput by remember { mutableStateOf("") }

    val quickChips = listOf(
        "Cara usap gusi yang benar?",
        "HP dipakai bersama keluarga, aman?",
        "Apa itu periode jendela 12 minggu?",
        "Uji Simulasi: Muncul 2 garis C dan T"
    )

    val deltaSe = uiState.dp2Record.se - uiState.dp1Record.se

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 2. Banner Status Dukungan Adaptif JITAI (Tabel 3.2 Proposal)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "JITAI: ${uiState.activeJitaiRule.decisionPoint} • ${uiState.activeJitaiRule.activeRuleCode}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "ΔSE: " + String.format("%+.1f", deltaSe),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (deltaSe > 0) Color(0xFF059669) else Color(0xFFD97706)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${uiState.activeJitaiRule.categoryName} — ${uiState.activeJitaiRule.recommendationTitle}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))
                // Vektor skor [PR, PC, ST, SE]
                Text(
                    text = "Vektor Waktu Nyata: [PR: ${String.format("%.1f", uiState.dp1Record.pr)}, PC: ${String.format("%.1f", uiState.dp1Record.pc)}, ST: ${String.format("%.1f", uiState.dp1Record.st)}, SE: ${String.format("%.1f", uiState.dp1Record.se)}]",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        // 3. Ruang Pesan Chat
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            reverseLayout = false
        ) {
            items(uiState.chatMessages) { msg ->
                ChatMessageBubble(
                    message = msg,
                    onContactCounselor = { onNavigateToModul() }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (uiState.isChatLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI sedang menganalisis panduan klinis & menyaring privasi...", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Quick Prompt Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(quickChips) { chipText ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable { onSendMessage(chipText) }
                ) {
                    Text(
                        text = chipText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Kotak Input Pesan dengan PII Scrubber realtime
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Tanyakan apa saja seputar HIVST & privasi...", fontSize = 12.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_chat_message"),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onSendMessage(textInput)
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .testTag("btn_send_chat")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Kirim",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    onContactCounselor: () -> Unit
) {
    val isUser = message.sender == MessageSender.USER
    val isSystem = message.sender == MessageSender.SYSTEM

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Notifikasi PII Scrubber jika ada data pribadi yang disensor
        if (message.wasPiiScrubbed) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEF3C7),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(10.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PII Terlindungi: ${message.scrubbedTypes.joinToString(", ")} telah disensor otomatis.",
                        fontSize = 9.sp,
                        color = Color(0xFF92400E),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Gelembung Pesan
        Surface(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isUser) 12.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 12.dp
            ),
            color = when {
                isUser -> MaterialTheme.colorScheme.primary
                isSystem -> MaterialTheme.colorScheme.surfaceVariant
                message.isEmergencyEscalation -> Color(0xFFFEF2F2)
                else -> MaterialTheme.colorScheme.surface
            },
            tonalElevation = if (isUser) 0.dp else 2.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (!isUser && !isSystem) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = if (message.isEmergencyEscalation) EmergencyRed else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (message.isEmergencyEscalation) "Eskalasi Klinis PKPR" else "Konselor Digital Adaptif",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (message.isEmergencyEscalation) EmergencyRed else MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.text,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Hard Rule Escalation Bypass LLM Card (Human-in-the-Loop)
        if (message.isEmergencyEscalation) {
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Dukungan Klinis Terjadwal: PKPR Puskesmas Tempuran",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF991B1B)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Jangan ragu dan jangan merasa sendiri. Tim konselor ramah remaja di Puskesmas Tempuran siap mendampingi Anda untuk tes konfirmasi 3 reagen secara GRATIS dan RAHASIA.",
                        fontSize = 11.sp,
                        color = Color(0xFF7F1D1D),
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onContactCounselor,
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_emergency_linkage")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Hubungi Konselor PKPR (Active Linkage)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlGroupLockedView(
    onNavigateToModul: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Klaster Edukasi Terstandar (Kontrol X = 0)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Akun Anda teralokasi pada Kelompok Kontrol penelitian ini. Selama Hari ke-1 s.d. Hari ke-10, Anda mendapatkan akses penuh ke materi Modul Bacaan dan Video Edukasi komprehensif.\n\nSesuai desain Waitlist Crossover, fitur Chatbot AI adaptif akan terbuka penuh setelah Anda menyelesaikan kuesioner Posttest pada Hari ke-10.",
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNavigateToModul,
            modifier = Modifier.testTag("btn_control_go_to_modul")
        ) {
            Text("Buka Pustaka E-Modul Bacaan")
        }
    }
}
