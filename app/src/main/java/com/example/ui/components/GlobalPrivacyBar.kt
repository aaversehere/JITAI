package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmergencyRed
import com.example.viewmodel.AppUiState

@Composable
fun GlobalPrivacyBar(
    uiState: AppUiState,
    onEmergencyCamouflageClick: () -> Unit,
    onSimulateDiscreetNotification: () -> Unit,
    onToggleDataRights: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = uiState.autoLogoutSecondsRemaining / 60
    val seconds = uiState.autoLogoutSecondsRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("global_privacy_bar"),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            // Baris 1: Token, Klaster, Hari, Timer, dan Tombol Cepat
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Info Token & Klaster
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (uiState.clusterX == 1) Color(0xFF10B981) else Color(0xFF3B82F6))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = uiState.userToken,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (uiState.clusterX == 1) "Intervensi (X=1) • Hari ke-${uiState.programDay}"
                            else "Kontrol (X=0) • Hari ke-${uiState.programDay}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }

                // Bagian Kanan: Timer, Dark Mode, Discreet Notif, dan Tombol Darurat
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Indikator Timer Auto-Logout
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer Auto Logout",
                                modifier = Modifier.size(12.dp),
                                tint = if (uiState.autoLogoutSecondsRemaining < 60) EmergencyRed else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formattedTime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.autoLogoutSecondsRemaining < 60) EmergencyRed else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Toggle Dark Mode
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier.size(32.dp).testTag("btn_toggle_dark_mode")
                    ) {
                        Icon(
                            imageVector = if (uiState.isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                            contentDescription = "Toggle Dark Mode",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Simulasi Discreet Notif
                    IconButton(
                        onClick = onSimulateDiscreetNotification,
                        modifier = Modifier.size(32.dp).testTag("btn_simulate_notif")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Simulasi Discreet Notif",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Menu Hak Privasi
                    IconButton(
                        onClick = onToggleDataRights,
                        modifier = Modifier.size(32.dp).testTag("btn_data_rights")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Hak Subjek Data",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Tombol Darurat Merah: Hapus Layar / Samaran Cepat
                    Button(
                        onClick = onEmergencyCamouflageClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmergencyRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("btn_emergency_decoy"),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Mode Samaran",
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Samaran", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DataRightsDialog(
    onDismiss: () -> Unit,
    onClearLocalChat: () -> Unit,
    onWithdrawConsent: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.Security, contentDescription = "Perlindungan Data", tint = MaterialTheme.colorScheme.primary)
        },
        title = {
            Text("Hak Subjek Data Pribadi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column {
                Text(
                    text = "Sebagai subjek data dalam penelitian ini, privasi dan kedaulatan Anda dijamin penuh:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "1. Hak Akses & Koreksi: Data hanya menggunakan kode pseudonim tanpa nama asli atau identitas langsung.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "2. Hak Penghapusan Riwayat: Anda dapat menghapus seluruh riwayat percakapan dan cache lokal kapan pun.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "3. Hak Menarik Persetujuan: Anda bebas mengundurkan diri sewaktu-waktu tanpa konsekuensi akademik atau penurunan nilai di sekolah.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onClearLocalChat()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_clear_chat_history"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Bersihkan Riwayat Percakapan Lokal")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        onWithdrawConsent()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_withdraw_consent"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed)
                ) {
                    Text("Tarik Persetujuan & Reset Sesi")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
